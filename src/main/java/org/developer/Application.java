package org.developer;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.developer.process.Processor;

public class Application {
    final static Logger logger = Logger.getLogger(Application.class);
    public static void main(String[] args){
        BasicConfigurator.configure();
        logger.info("Begin :: Application");
        Processor processor = new Processor();
        processor.process();
        logger.info("End :: Application");

    }
}
