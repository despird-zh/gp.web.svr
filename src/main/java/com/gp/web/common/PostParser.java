package com.gp.web.common;

import com.gp.common.GeneralConfig;
import com.gp.common.IdKey;
import com.gp.common.Images;
import com.gp.common.SystemOptions;
import com.gp.core.CommonFacade;
import com.gp.info.InfoId;
import com.gp.util.ConfigSettingUtils;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * PostParser parse the content of post and extract the excerpt of post.
 *
 * @author gary diao
 * @version v0.1 2015-12-22
 **/
public class PostParser implements NodeVisitor{

    List<String> images = new ArrayList<>();

    static String CachePath = ConfigSettingUtils.getSystemOption(SystemOptions.FILE_CACHE_PATH);
    static String ImagePath = ConfigSettingUtils.getSystemOption(SystemOptions.IMAGE_CACHE_PATH);

    private static final int maxWidth = 80;
    private int width = 0;
    private StringBuilder content = new StringBuilder(); // holds the accumulated text
    private StringBuilder excerpt = new StringBuilder();
    private Element excerptImg = null;
    /**
     * Constructor with postContent.
     * @param htmlcode the content of post
     **/
    public PostParser(String htmlcode){

        // parse the html code.
        Document doc = Jsoup.parseBodyFragment(htmlcode);
        Element body = doc.body();

        NodeTraversor traversor = new NodeTraversor(this);
        traversor.traverse(body); // walk the DOM, and call .head() and .tail() for each node
        content.append(body.html());
        // cut off the extra text
        if(excerpt.length() > 160){
        	excerpt.delete(160, excerpt.length());
        }
        // wrap the excerpt with p tag.
        excerpt.insert(0, "<p>").append("</p>");
        if(excerptImg != null ) {
            excerptImg.addClass("excerpt-img");
            excerptImg.removeAttr("style");
            // prepend the html code to excerpt
            excerpt.insert(0, excerptImg.outerHtml());
        }
    }

    /**
     * Get the image list that reserved in cache folder
     * @return List of images
     **/
    public List<String> getPostImages(){
        return this.images;
    }

    public String getPostContent(){

        return this.content.toString();
    }

    public String getPostExcerpt(){

        return this.excerpt.toString();
    }

    /** hit when the node is first seen
     * Traverse the head of node
     */
    public void head(Node node, int depth) {

        String name = node.nodeName();
        if (node instanceof TextNode) {
            append(((TextNode) node).text()); // TextNodes carry all user-readable text in the DOM.
        }else if (name.equals("li")) {
            append("\n * ");
        }else if (name.equals("dt")) {
            append("  ");
        }else if (StringUtil.in(name, "p", "h1", "h2", "h3", "h4", "h5", "tr")) {
            append("\n");
        }else if (name.equals("img")){

            if(null == excerptImg) {
                excerptImg = (Element) node;
            }
            String src = node.attr("src");
            // as for embedded image will extract it
            if(src.startsWith("data:image/")){

                int separatorPost = src.indexOf(',');
                // prefix to be : data:image/gif;base64,R0lG....
                String prefix = src.substring(0 , separatorPost);
                InfoId<Long> imgid = CommonFacade.generateId(IdKey.IMAGE, Long.class);
                String file_ext = prefix.substring(prefix.indexOf('/') + 1, prefix.indexOf(';'));
                // the file name : {id}-{date}.{ext}
                String file_name = Images.getImgFileName(imgid.getId(), file_ext);
                // convert the base64 String into bytes data
                byte[] decodedString = Base64.decodeBase64(src.substring(separatorPost + 1, src.length()));
                // create image file in ImageCache folder
                try(
                    ByteArrayInputStream is = new ByteArrayInputStream(decodedString);
                    FileOutputStream os = new FileOutputStream(
                        new File(CachePath + File.separator + ImagePath + File.separator + file_name)
                    )
                ){

                    byte[] buffer = new byte[4096];
                    int length;

                    // copy the file content in bytes
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }

                    images.add(file_name);

                    node.attr("src", "../" + ImagePath + "/" + file_name);

                }catch(Exception ex){
                    ex.printStackTrace();
                }

            }
        }
    }

    /**
     * Hit when all of the node's children (if any) have been visited
     * @param node the node of element
     * @param depth the depth of element tree
     */
    public void tail(Node node, int depth) {
        String name = node.nodeName();
        if (StringUtil.in(name, "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5")) {
            append("\n");
        }else if (name.equals("a")) {
            append(String.format(" <%s>", node.absUrl("href")));
        }
    }

    /**
     * Appends text to the string builder with a simple word wrap method
     * @param  text the text of node
     */
    private void append(String text) {
        if (text.startsWith("\n"))
            width = 0; // reset counter if starts with a newline. only from formats above, not in natural text
        if (text.equals(" ") &&
                (excerpt.length() == 0 || StringUtil.in(excerpt.substring(excerpt.length() - 1), " ", "\n")))
            return; // don't accumulate long runs of empty spaces

        if (text.length() + width > maxWidth) { // won't fit, needs to wrap
            String words[] = text.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                boolean last = i == words.length - 1;
                if (!last) // insert a space if not the last word
                    word = word + " ";
                if (word.length() + width > maxWidth) { // wrap and reset counter
                    excerpt.append("\n").append(word);
                    width = word.length();
                } else {
                    excerpt.append(word);
                    width += word.length();
                }
            }
        } else { // fits as is, without need to wrap text
            excerpt.append(text);
            width += text.length();
        }
    }

}
