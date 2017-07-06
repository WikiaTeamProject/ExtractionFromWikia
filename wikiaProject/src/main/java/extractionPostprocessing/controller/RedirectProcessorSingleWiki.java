package extractionPostprocessing.controller;

import extraction.Extractor;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class processes the redirect file of a single wiki.
 */
public class RedirectProcessorSingleWiki {

    private HashMap<String, String> redirectsMap = new HashMap<>();
    private static Logger logger = Logger.getLogger(RedirectProcessorSingleWiki.class.getName());
    private File wikiDirectory;


    /**
     * Constructor
     *
     * @param filePathToWiki File path to the wiki files.
     */
    public RedirectProcessorSingleWiki(String filePathToWiki) {
        setWikiDirectory(filePathToWiki);
    }

    /**
     * Constructor
     *
     * @param wikiDirectory Directory where the files of the wiki can be found.
     */
    public RedirectProcessorSingleWiki(File wikiDirectory) {
        setWikiDirectory(wikiDirectory);
    }


    /**
     * Read the redirect file.
     *
     * @return Returns true if reading was successful, else false.
     */
    public boolean readRedirects() {

        // make sure the directory actually is a directory
        if (!wikiDirectory.isDirectory() || wikiDirectory == null) {
            logger.severe("Given directory does not lead to a directory. Use the corresponding setter method to set the correct path.");
            return false;
        }

        // get the redirect file
        File redirectFile = null;
        for (File f : wikiDirectory.listFiles()) {
            if (f.getName().endsWith("-redirects.ttl")) {
                // -> redirects file found
                redirectFile = f;
                logger.info("Reading from file " + f.getName());
                break;
            }
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(redirectFile));

            String line;
            Matcher matcher = null;
            while ((line = br.readLine()) != null) {
                logger.info(line);
                Pattern pattern = Pattern.compile("<[^<]*>");
                // regex: <[^<]*>
                // this regex captures everything between tags including the tags: <...>
                // there are three tags in every line

                matcher = pattern.matcher(line);

                int index = 0;
                String key = null;
                String value = null;

                while (matcher.find()) {
                    index++;
                    switch (index) {
                        case 1:
                            // first match: key
                            key = matcher.group();
                            logger.info("Key: " + matcher.group());
                            break;
                        // (second match: "<http://dbpedia.org/ontology/wikiPageRedirects>"
                        // always the same -> irrelevant for us
                        case 3:
                            value = matcher.group();

                            if (key != null && value != null) {
                                // key and value found -> add to map and break while loop
                                logger.info("Value:  " + matcher.group());

                                redirectsMap.put(key, value);
                                key = null;
                                value = null;
                                break;
                            }
                            break;
                    }

                }
            } // end of while ((line = br.readLine()) != null)
            br.close();

        } catch (IOException ioe) {
            logger.severe(ioe.toString());
            return false;
        }
        return true;
    }


    /**
     * Replace all synonyms of resources using the redirect file.
     * Method {@link #readRedirects()} will be called if it has not been called before.
     *
     * @return
     */
    public boolean executeRedirects() {

        // check whether the redirects were already read.
        // if not read them.
        if (redirectsMap.isEmpty()) {
            if (!this.readRedirects()) {
                // redirects could not be read
                logger.severe("Redirects could not be read.");
                return false;
            }
        }

        // update label.ttl file with redirects
        updateLabelFile();

        BufferedReader reader;
        boolean changeOccurred;
        int counter = 1;
        do {
            logger.info("Iteration number: " + counter++);
            changeOccurred = false; // no change has yet occurred
            StringBuffer newFileContent = new StringBuffer();

            File[] fileList = wikiDirectory.listFiles((dir, name) -> {
                if (name.endsWith(".ttl") && !name.endsWith("-redirects.ttl") && !name.endsWith("labels.ttl")) {
                    // we do not want to process the redirects file itself, labels file already processed
                    logger.info("Skipped: " + name);
                    return true;
                }
                return false;
            });

            for (File f : fileList) {
                // -> we are interested in the file
                logger.info("Processing: " + f.getName());

                try {
                    reader = new BufferedReader(new FileReader(f));

                    String line;
                    Matcher matcher = null;

                    while ((line = reader.readLine()) != null) {
                        Pattern pattern = Pattern.compile("<[^<]*>");
                        // regex: <[^<]*>
                        // this regex captures everything between tags including the tags: <...>
                        // there are three tags in every line, we are not interested in the second tag

                        matcher = pattern.matcher(line);

                        int index = 0;
                        String key = null;
                        String value = null;

                        while (matcher.find()) {
                            index++;
                            switch (index) {
                                case 1:
                                    if (redirectsMap.containsKey(matcher.group())) {
                                        // there is something to replace
                                        changeOccurred = true;

                                        // replace operation
                                        line = line.replace(matcher.group(), (String) redirectsMap.get(matcher.group()));
                                    }
                                    break;
                                // (second match: "<some interlinking tag>" -> irrelevant for us
                                case 3:
                                    if (redirectsMap.containsKey(matcher.group())) {
                                        // there is something to replace
                                        changeOccurred = true;

                                        // replace operation
                                        line = line.replace(matcher.group(), (String) redirectsMap.get(matcher.group()));
                                    }
                                    break;
                            }

                        }
                        newFileContent.append(line + "\n"); // the line break must be added
                    }
                    reader.close();
                } catch (IOException ioe) {
                    logger.severe(ioe.toString());
                }

                // write the new file content into the file if a change occurred
                if (changeOccurred) {
                    File newFile = new File(f.getAbsolutePath());
                    logger.info("Re-Writing File: " + newFile.getName());
                    try {
                        FileWriter writer = new FileWriter(newFile);
                        writer.write(newFileContent.toString());
                        writer.flush();
                        writer.close();
                    } catch (IOException ioe) {
                        logger.severe(ioe.toString());
                    }

                    // delete the content after it was written
                    newFileContent = new StringBuffer();
                }

            } // end of if ( file endsWith("ttl") )


            if (counter > 10) {
                logger.warning("Redirect processing was cancelled after 10 iterations. There is probably an infinite loop relation in the redirects file.");
                return false;
            }

        } while (changeOccurred);

        logger.info("Number of iterations needed: " + (counter - 1));

        return true;
    }

    /**
     * Update label.ttl file with redirects file and skos properties (skos:prefLabel, skos:altLabel)
     */
    public void updateLabelFile() {

        File[] fileList = wikiDirectory.listFiles((dir, name) -> {
            if (name.endsWith("labels.ttl")) return true;
            return false;
        });

        // fileList only contains labels.ttl file
        for (File f : fileList) {

            StringBuffer newFileContent = new StringBuffer();

            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));

                String line;
                Matcher matcher;
                String key = null;
                String redirect;

                while ((line = reader.readLine()) != null) {
                    Pattern pattern = Pattern.compile("<[^<]*>");
                    // regex: <[^<]*>
                    // this regex captures everything between tags including the tags: <...>
                    // there are only two tags in every line (replace second)
                    // e.g. <http://uni-mannheim.de/resource/HBO> <http://www.w3.org/2000/01/rdf-schema#label> "HBO"@en .

                    matcher = pattern.matcher(line);
                    redirect = null;

                    int index = 0;

                    while (matcher.find()) {
                        index++;

                        switch (index) {
                            case 1:
                                key = matcher.group();
                                if ((redirect = getRedirect(key)) != null) {

                                    // replace tag with redirect
                                    line = line.replace(key, redirect);
                                }
                                break;

                            // second match: <http://www.w3.org/2000/01/rdf-schema#label>
                            // replace if redirect exists with either:
                            // skos:prefLabel <http://www.w3.org/2004/02/skos/core#prefLabel>
                            // or skos:altLabel <http://www.w3.org/2004/02/skos/core#altLabel>
                            case 2:
                                if ((!redirectsMap.containsKey(key) && redirectsMap.containsValue(key)) || (redirect != null && key == redirect)) {

                                    // replace tag with pref label
                                    line = line.replace(matcher.group(), "<http://www.w3.org/2004/02/skos/core#prefLabel>");

                                } else if (redirect != null) {

                                    // replace tag with alt label
                                    line = line.replace(matcher.group(), "<http://www.w3.org/2004/02/skos/core#altLabel>");
                                }
                                break;
                        }

                    }
                    newFileContent.append(line + "\n"); // the line break must be added
                }
                reader.close();
            } catch (IOException ioe) {
                logger.severe(ioe.toString());
            }

            // write the new file content into the file if a change occurred
            File newFile = new File(f.getAbsolutePath());
            logger.info("Re-Writing File: " + newFile.getName());
            try {
                FileWriter writer = new FileWriter(newFile);
                writer.write(newFileContent.toString());
                writer.flush();
                writer.close();
            } catch (IOException ioe) {
                logger.severe(ioe.toString());
            }

            // delete the content after it was written
            newFileContent = new StringBuffer();

        }
    }

    /**
     * Get latest redirect from redirectsMap for a specific key.
     *
     * @param key
     * @return
     */
    private String getRedirect(String key) {

        String input = key;
        String value = null;
        boolean changeOccurred;
        int counter = 1;


        do {
            changeOccurred = false; // no change has yet occurred

            if (redirectsMap.containsKey(key)) {
                changeOccurred = true;
                counter++;
                value = redirectsMap.get(key);
                key = value;
            }

            if (counter > 10) {
                logger.warning("Redirect processing was cancelled after 10 iterations. There is probably an infinite loop relation in the redirects file.");
                changeOccurred = false;
            }

        } while (changeOccurred);

        logger.fine("Labels.ttl: redirect for " + input + " is " + value);

        return value;
    }


    /**
     * Prints the redirects map in a readable format on the console.
     */
    public void printRedirectsMapOnConsole() {
        HashMap.Entry entry;
        Iterator iterator = redirectsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            entry = (HashMap.Entry) iterator.next();
            logger.info("Key: " + entry.getKey());
            logger.info("Value " + entry.getValue());
        }
    }


    //
    // IN THE FOLLOWING YOU WILL ONLY FIND GETTERS AND SETTERS
    //

    public String getWikiDirectoryPath() {
        return wikiDirectory.getPath();
    }

    public boolean setWikiDirectory(String filePathToWiki) {
        File wikiDirectoryCandidate = new File(filePathToWiki);

        // make sure the directory actually is a directory
        if (!wikiDirectoryCandidate.isDirectory()) {
            logger.severe("Given filePathToWiki does not lead to a directory.");
            return false;
        } else {
            this.wikiDirectory = wikiDirectoryCandidate;
        }

        // when a new wiki is set, the redirectsMap is not valid any more
        redirectsMap = new HashMap<>();
        ;
        return true;
    }

    public boolean setWikiDirectory(File wikiDirectory) {
        if (!wikiDirectory.isDirectory()) {
            logger.severe("Given filePathToWiki does not lead to a directory.");
            return false;
        } else {
            this.wikiDirectory = wikiDirectory;
        }
        // when a new wiki is set, the redirectsMap is not valid any more
        redirectsMap = new HashMap<>();
        return true;
    }

    public File getWikiDirectory() {
        return wikiDirectory;
    }

}
