package extractionPostprocessing.controller;

import extraction.Extractor;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class processes the redirect file.
 */
public class RedirectProcessor {

    private HashMap<String, String> redirectsMap = new HashMap<>();
    private static Logger logger = Logger.getLogger(Extractor.class.getName());
    private File wikiDirectory;


    /**
     * Constructor
     * @param filePathToWiki File path to the wiki files.
     */
    public RedirectProcessor(String filePathToWiki) {
        setWikiDirectory(filePathToWiki);
    }

    /**
     * Constructor
     *
     * @param wikiDirectory Directory where the files of the wiki can be found.
     */
    public RedirectProcessor(File wikiDirectory) {
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
                System.out.println(line);
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
                    if (index == 1) {
                        // first match: key
                        key = matcher.group();
                        System.out.println("Key: " + matcher.group());
                    }

                    // (second match: "<http://dbpedia.org/ontology/wikiPageRedirects>"
                    // always the same -> irrelevant for us

                    if (index == 3) {
                        value = matcher.group();
                        System.out.println(matcher.group());

                        if (key != null && value != null) {
                            // key and value found -> add to map and break while loop
                            System.out.println("Value:  " + matcher.group());

                            redirectsMap.put(key, value);
                            key = null;
                            value = null;
                            break;
                        }
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

        BufferedReader reader = null;
        boolean changeOccurred = false;
        int counter = 0;
        do {
            System.out.println("Iteration number " + counter++);
            changeOccurred = false; // no change has yet occurred
            StringBuffer newFileContent = new StringBuffer();

            for (File f : wikiDirectory.listFiles()) {
                if (f.getName().endsWith(".ttl")) {
                    // -> we are interested in the file
                    System.out.println(f.getName());

                    if (f.getName().endsWith("-redirects.ttl")) {
                        // we do not want to process the redirects file itself
                        continue;
                    }

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
                                if (index == 1) {
                                    if (redirectsMap.containsKey(matcher.group())) {
                                        // there is something to replace
                                        changeOccurred = true;

                                        // replace operation
                                        //System.out.println("Old line: " + line);
                                        line = line.replace(matcher.group(), (String) redirectsMap.get(matcher.group()));
                                        //System.out.println("New line: " + line);
                                    }
                                }

                                // (second match: "<some interlinking tag>" -> irrelevant for us

                                if (index == 3) {
                                    if (redirectsMap.containsKey(matcher.group())) {
                                        // there is something to replace
                                        changeOccurred = true;

                                        // replace operation
                                        //System.out.println("Old line: " + line);
                                        line = line.replace(matcher.group(), (String) redirectsMap.get(matcher.group()));
                                        //System.out.println("New line: " + line);
                                    }
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
                        System.out.println("Re-Writing File :" + newFile.getName());
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

            } // end of for loop through all files in the directory

        } while (changeOccurred);

        System.out.println("Number of iterations: " + (counter - 1));

        return true;
    }


    /**
     * Prints the redirects map in a readable format on the console.
     */
    public void printRedirectsMapOnConsole() {
        HashMap.Entry entry;
        Iterator iterator = redirectsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            entry = (HashMap.Entry) iterator.next();
            System.out.println("Key: " + entry.getKey());
            System.out.println("Value " + entry.getValue());
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
