package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class Task {

    private static String CHARSET_NAME = "utf8";

    public static void main(String[] args) throws Exception {

        System.out.println("Program Arguments:");
        for (String arg : args) {
            System.out.println("\t" + arg);
        }

        File inputSample = new File(args[0]);
        File inputSample2 = new File(args[1]);
        String targetElementId = "make-everything-ok-button";
        Element originButtonElement = null;
        List<String> originButtonPath = new ArrayList<>();
        Elements foundButtonElements = null;
        Element foundButtonElement = null;
        List<String> foundButtonPath = new ArrayList<>();

        if (inputSample.exists() && inputSample2.exists()) {
            //to save the buttons information
            ButtonInfo originButton = new ButtonInfo();
            ButtonInfo foundButton = new ButtonInfo();

            Optional<Element> doc = findElementById(inputSample, targetElementId);
            //check Optional for not null
            if (doc.isPresent()) {
                originButtonElement = doc.get();
            }

            originButton.setButtonTag(originButtonElement.tag().toString());
            originButton.setButtonClass(originButtonElement.attr("class"));
            originButton.setButtonHref(originButtonElement.attr("href"));
            originButton.setButtonTitle(originButtonElement.attr("title"));
            originButton.setButtonRel(originButtonElement.attr("rel"));
            originButton.setButtonText(originButtonElement.text());
            originButton.setButtonOnClick(originButtonElement.attr("onclick"));

            System.out.println(System.lineSeparator() + "Element found from origin sample by id:");
            System.out.println("tag : " + originButtonElement.tag());
            System.out.println("class : " + originButton.getButtonClass());
            System.out.println("href : " + originButton.getButtonHref());
            System.out.println("title : " + originButton.getButtonTitle());
            System.out.println("rel : " + originButton.getButtonRel());
            System.out.println("content : " + originButton.getButtonText());
            System.out.println("onclick : " + originButton.getButtonOnClick());

            //get the list of all parents of the element, save their tags to the list
            Elements parentsNames = originButtonElement.parents();
            for (Element tag : parentsNames) {
                originButtonPath.add(tag.tagName());
                //System.out.println("parent " + tag.tagName() + " class " + tag.className());
            }

            System.out.println(System.lineSeparator() + "Element found from origin sample path:");
            //save path to the button object
            for (int i = originButtonPath.size() - 1; i >= 0; i--) {
                System.out.print("->" + originButtonPath.get(i));
                originButton.addButtonPathElement(originButtonPath.get(i));
            }
            System.out.print(System.lineSeparator());
            //get a first "word" of the class 
            String commonClass = originButton.getButtonClass();
            if (commonClass.contains(" ")) {
                commonClass = commonClass.substring(0, commonClass.indexOf(" "));
            }
            //get a text from href
            String commonStr = removeNodigitsNoLetters(originButton.getButtonHref());
            //originButtonElement.tag()+"[class*=\"btn\"][href*=\"ok\"][onclick*=\"ok\"]"
            String cssQuery = originButtonElement.tag() + "[class*=\"" + commonClass + "\"][href*=\"" + commonStr
                    + "\"][onclick*=\"" + commonStr + "\"]";

            Optional<Elements> elementsOpt = findElementsByQuery(inputSample2, cssQuery);
            // Check Optional for not null
            if (elementsOpt.isPresent()) {
                foundButtonElements = elementsOpt.get();
            }
            System.out.println(System.lineSeparator() + "Element found in other sample:");

            for (Element els : foundButtonElements) {
                System.out.println("class : " + els.attr("class"));
                System.out.println("href : " + els.attr("href"));
                System.out.println("title : " + els.attr("title"));
                System.out.println("rel : " + els.attr("rel"));
                System.out.println("content : " + els.text());
                System.out.println("onclick : " + els.attr("onclick"));
            }
            foundButtonElement = foundButtonElements.get(0);
            //get the list of all parents of the element, save their tags to the list
            Elements foundBtnParentsNames = foundButtonElement.parents();
            for (Element tag : foundBtnParentsNames) {
                foundButtonPath.add(tag.tagName());
            }
            System.out.println(System.lineSeparator() + "Element found in other sample path:");
            //save path to the button object
            for (int i = foundButtonPath.size() - 1; i >= 0; i--) {
                System.out.print("->" + foundButtonPath.get(i));
                foundButton.addButtonPathElement(foundButtonPath.get(i));
            }
            System.out.print(System.lineSeparator());
            //save to found button class
            foundButton.setButtonTag(foundButtonElement.tag().toString());
            foundButton.setButtonClass(foundButtonElement.attr("class"));
            foundButton.setButtonHref(foundButtonElement.attr("href"));
            foundButton.setButtonTitle(foundButtonElement.attr("title"));
            foundButton.setButtonRel(foundButtonElement.attr("rel"));
            foundButton.setButtonText(foundButtonElement.text());
            foundButton.setButtonOnClick(foundButtonElement.attr("onclick"));
        } else {
            String message = "Cannot find input files...";
            System.out.println(message);
            throw new FileNotFoundException(message);
        }
    }

    private static String removeNodigitsNoLetters(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetterOrDigit(s.charAt(i)))
                sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    private static Optional<Element> findElementById(File htmlFile, String targetElementId) {
        try {
            Document doc = Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath());

            return Optional.of(doc.getElementById(targetElementId));

        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private static Optional<Elements> findElementsByQuery(File htmlFile, String cssQuery) {
        try {
            Document doc = Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath());

            return Optional.of(doc.select(cssQuery));

        } catch (IOException e) {
            return Optional.empty();
        }
    }

}

class ButtonInfo {
    private String buttonTag;
    private String buttonClass;
    private String buttonHref;
    private String buttonTitle;
    private String buttonRel;
    private String buttonOnClick;
    private String buttonText;
    private List<String> buttonPath = new ArrayList<>();

    public List<String> getButtonPath() {
        return buttonPath;
    }

    public void setButtonPath(List<String> buttonPath) {
        if (buttonPath != null)
            this.buttonPath.addAll(buttonPath);
    }

    public void addButtonPathElement(String buttonPathElement) {
        this.buttonPath.add(buttonPathElement);
    }

    public String getButtonClass() {
        return buttonClass;
    }

    public void setButtonClass(String buttonClass) {
        this.buttonClass = buttonClass;
    }

    public String getButtonHref() {
        return buttonHref;
    }

    public void setButtonHref(String buttonHref) {
        this.buttonHref = buttonHref;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }

    public String getButtonRel() {
        return buttonRel;
    }

    public void setButtonRel(String buttonRel) {
        this.buttonRel = buttonRel;
    }

    public String getButtonOnClick() {
        return buttonOnClick;
    }

    public void setButtonOnClick(String buttonOnClick) {
        this.buttonOnClick = buttonOnClick;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonTag() {
        return buttonTag;
    }

    public void setButtonTag(String buttonTag) {
        this.buttonTag = buttonTag;
    }

}