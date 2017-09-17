package applications.extractionPostprocessing.controller;

import loggingService.MessageLogger;
import org.apache.log4j.Level;
import utils.IOoperations;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class processes the redirect file of a single wiki.
 * It also takes care of the labels.ttl file and sets preferred and alternate labels.
 */
public class RedirectProcessorSingleWiki {

    private HashMap<String, String> redirectsMap = new HashMap<>();
    private static MessageLogger logger=new MessageLogger();
    private static final String MODULE="ExtractionPostprocessing";
    private static final String CLASS=RedirectProcessorSingleWiki.class.getName();
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
            logger.logMessage(Level.FATAL,MODULE,CLASS,"Given directory does not lead to a directory. Use the corresponding setter method to set the correct path.");
            return false;
        }

        // get the redirect file
        File redirectFile = null;
        for (File f : wikiDirectory.listFiles()) {
            if (f.getName().endsWith("-redirects.ttl")) {
                // -> redirects file found
                redirectFile = f;
                logger.logMessage(Level.INFO,MODULE,CLASS,"Reading from file " + f.getName());
                break;
            }
        }

        try {

            BufferedReader br = new BufferedReader(new FileReader(redirectFile));

            String line;
            Matcher matcher = null;
            while ((line = br.readLine()) != null) {
                logger.logMessage(Level.INFO,MODULE,CLASS,line);
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
//                            logger.info("Key: " + matcher.group());
                            break;
                        // (second match: "<http://dbpedia.org/ontology/wikiPageRedirects>"
                        // always the same -> irrelevant for us
                        case 3:
                            value = matcher.group();

                            if (key != null && value != null) {
                                // key and value found -> add to map and break while loop
//                                logger.info("Value:  " + matcher.group());

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
            logger.logMessage(Level.FATAL,MODULE,CLASS,ioe.toString());
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
                logger.logMessage(Level.FATAL,MODULE,CLASS,"DBpediaResourceServiceOffline could not be read.");
                return false;
            }
        }

        // update label.ttl file with redirects
        updateLabelFile();

        BufferedReader reader;

        StringBuffer newFileContent = new StringBuffer();

        File[] fileList = wikiDirectory.listFiles((dir, name) -> {
            if (name.endsWith(".ttl") && !name.endsWith("-redirects.ttl") && !name.endsWith("labels.ttl")) {
                // we do not want to process the redirects file itself, labels file is already processed
                return true;
            }

            logger.logMessage(Level.INFO,MODULE,CLASS,"Skipped: " + name);
            return false;
        });

        for (File f : fileList) {
            // -> we are interested in the file
            logger.logMessage(Level.INFO,MODULE,CLASS,"Processing: " + f.getName());

            try {
                reader = new BufferedReader(new FileReader(f));

                String line;
                Matcher matcher;

                while ((line = reader.readLine()) != null) {

                    if (! f.getName().contains("homepages.ttl")) {
                        // replace wikipedia links with actual wiki links
                        line = line.replace("en.wikipedia.org", wikiDirectory.getName() + ".wikia.com");

                        // replace commons wikimedia links with actual wiki links to files
                        if (line.contains("commons.wikimedia.org")) {
                            line = line.replace("commons.wikimedia.org", wikiDirectory.getName() + ".wikia.com");
                            line = line.replace("Special:FilePath/", "File:");
                        }
                    }

                    Pattern pattern = Pattern.compile("<[^<]*>");
                    // regex: <[^<]*>
                    // this regex captures everything between tags including the tags: <...>
                    // there are three tags in every line, we are not interested in the second tag

                    matcher = pattern.matcher(line);
                    int index = 0;
                    boolean isType = false, excludeLine = false;

                    while (matcher.find()) {
                        index++;

                        switch (index) {
                            // first and last tag replaced with redirect if one exists
                            case 1:
                                if (redirectsMap.containsKey(matcher.group())) {
                                    // replace operation
                                    line = line.replace(matcher.group(), getRedirect(matcher.group()));
                                }
                                break;
                            // second match: "<some interlinking tag>"
                            // if it is of type rdf-schema type, then only keep depending on object
                            case 2:
                                if (matcher.group().contains("rdf-syntax-ns#type") && ! f.getName().contains("property-definitions.ttl"))
                                    isType = true;
                                break;
                            case 3:
                                // only include specific objects
                                if (isType && ! (matcher.group().contains("dbpedia.org") || matcher.group().contains("foaf/0.1/Document") || matcher.group().contains("core#Concept"))) {
                                    excludeLine = true;
                                } else if (redirectsMap.containsKey(matcher.group())) {
                                    // replace operation
                                    line = line.replace(matcher.group(), getRedirect(matcher.group()));
                                }
                                break;
                        }

                    }
                        if (! excludeLine) {
                            newFileContent.append(line + "\n"); // the line break must be added
                        }
                }
                reader.close();
            } catch (IOException ioe) {
                logger.logMessage(Level.FATAL,MODULE,CLASS,ioe.toString());
            }

            // write the new file content into the file if a change occurred
            File newFile = new File(f.getAbsolutePath());
            logger.logMessage(Level.INFO,MODULE,CLASS,"Re-Writing File: " + newFile.getName());
            IOoperations.writeContentToFile(newFile, newFileContent.toString());

            // delete the content after it was written
            newFileContent = new StringBuffer();
        }

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
                            case 2:
                                if ((!redirectsMap.containsKey(key) && redirectsMap.containsValue(key)) || (redirect != null && key == redirect)) {
                                    // replace tag with skos:prefLabel <http://www.w3.org/2004/02/skos/core#prefLabel>
                                    line = line.replace(matcher.group(), "<http://www.w3.org/2004/02/skos/core#prefLabel>");

                                } else if (redirect != null) {
                                    // replace tag with skos:altLabel <http://www.w3.org/2004/02/skos/core#altLabel>
                                    line = line.replace(matcher.group(), "<http://www.w3.org/2004/02/skos/core#altLabel>");
                                }
                                break;
                        }

                    }
                    newFileContent.append(line + "\n"); // the line break must be added
                }
                reader.close();
            } catch (IOException ioe) {
                logger.logMessage(Level.FATAL,MODULE,CLASS,ioe.toString());
            }

            // write the new file content into the file if a change occurred
            File newFile = new File(f.getAbsolutePath());
            logger.logMessage(Level.INFO,MODULE,CLASS,"Re-Writing File: " + newFile.getName());
            IOoperations.writeContentToFile(newFile, newFileContent.toString());
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
            // no change has yet occurred
            changeOccurred = false;

            if (redirectsMap.containsKey(key)) {
                changeOccurred = true;
                counter++;
                value = redirectsMap.get(key);
                key = value;
            }

            if (counter > 10) {
                logger.logMessage(Level.WARN,MODULE,CLASS,"Redirect processing was cancelled after 10 iterations. There is probably an infinite loop relation in the redirects file.");
                changeOccurred = false;
            }

        } while (changeOccurred);

        logger.logMessage(Level.DEBUG,MODULE,CLASS,"Labels.ttl: redirect for " + input + " is " + value);

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

            logger.logMessage(Level.INFO,MODULE,CLASS,"Key: " + entry.getKey());
            logger.logMessage(Level.INFO,MODULE,CLASS,"Value " + entry.getValue());
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
            logger.logMessage(Level.FATAL,MODULE,CLASS,"Given filePathToWiki does not lead to a directory.");
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
            logger.logMessage(Level.FATAL,MODULE,CLASS,"Given filePathToWiki does not lead to a directory.");
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
