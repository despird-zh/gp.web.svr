package gp.extra;

import com.google.common.collect.Lists;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * http://code.google.com/p/guava-libraries/wiki/HashingExplained
 * stackoverflow.com/questions/12319560/how-should-i-use-guavas-hashingconsistenthash
 */
public class GuavaHashing {
    private static final int N = 2500;

    public static void main(String[] args) throws IOException {
        List<String> ids = generateStoryIds(N);
        Set<String> testIds = generateTest(ids);
        bloomfiltertime(ids, testIds);
    }

    private static List<String> generateStoryIds(int size) {
        List<String> stories = new ArrayList<>();
        for (int i=0; i<size; ++i) {
            stories.add(RandomStringUtils.randomAlphanumeric(16));
        }
        return stories;
    }

    private static Set<String> generateTest(List<String> presList) {
        Set<String> test = new HashSet<>();
        Random rand = new Random(System.currentTimeMillis());
        for (int i=0; i<200; ++i) {
            test.add(presList.get(Math.abs(rand.nextInt()%N)));
        }
        for (int i=0; i<250; ++i) {
            test.add(RandomStringUtils.randomAlphanumeric(16));
        }
        return test;
    }

    public static void bloomfiltertime(List<String> storyIds, Set<String> testPresent) throws IOException {
        BloomFilter<String> stories = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), N, 0.05);
        long startTime = System.currentTimeMillis();
        for(String story : storyIds) {
            stories.put(story);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("bloom put time " + (endTime - startTime));

        FileOutputStream fos = new FileOutputStream("testfile.dat");
        stories.writeTo(fos);
        fos.close();

        FileInputStream fis = new FileInputStream("testfile.dat");
        BloomFilter<String> readStories = BloomFilter.readFrom(fis, Funnels.stringFunnel(Charset.defaultCharset()));
        startTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis();
        System.out.println("bloom read file time " + (endTime - startTime));

        startTime = System.currentTimeMillis();
        int count = 0;
        for(String story : testPresent) {
            if(stories.mightContain(story) != readStories.mightContain(story)) {
                ++count;
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println("bloom check time " + (endTime - startTime));
        System.out.println("varying : " + count);
        
        byte[] bytes = FileUtils.readFileToByteArray(new File("testfile.dat"));
        
        String content = Base64.getEncoder().encodeToString(bytes);
        System.out.println("content : " + content);
        
    }
}
