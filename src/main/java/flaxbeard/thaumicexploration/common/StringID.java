package flaxbeard.thaumicexploration.common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Katrina on 07/06/2015.
 */
public class StringID {

public static String getName()
{
    String Output="";
    try {
        List<String> adjectivesFile= FileUtils.readLines(new File(StringID.class.getClassLoader().getResource("assets/thaumicexploration/misc/adjectives").getFile()));
        List<String> animalsFile= FileUtils.readLines(new File(StringID.class.getClassLoader().getResource("assets/thaumicexploration/misc/animals").getFile()));

        Random rand=new Random();

        int num=rand.nextInt(adjectivesFile.size()-1);
        Output=Output+adjectivesFile.get(num).substring(0,1).toUpperCase()+adjectivesFile.get(num).substring(1);

        num=rand.nextInt(adjectivesFile.size()-1);
        Output=Output+adjectivesFile.get(num).substring(0,1).toUpperCase()+adjectivesFile.get(num).substring(1);

        num=rand.nextInt(animalsFile.size()-1);
        Output=Output+animalsFile.get(num).substring(0,1).toUpperCase()+animalsFile.get(num).substring(1);

    } catch (IOException e) {
        e.printStackTrace();
    }


    return Output;
}
}
