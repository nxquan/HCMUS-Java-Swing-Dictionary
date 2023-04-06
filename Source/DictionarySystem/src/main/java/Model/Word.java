package Model;

import java.io.Serializable;

public class Word implements Serializable {

    String word;
    String meaning;

    public Word(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String parse() {
        String result = "<span color=red>Word: </span>" + this.getWord()
                + "<br/>";

        String currentMeaning = this.getMeaning();
        if (currentMeaning.contains("(Tech)")) {
            int index = currentMeaning.indexOf("(Tech)");
            result = result + "<span color=red>Meaning:</span> "
                    + currentMeaning.substring(0, index - 2)
                    + "<br/>"
                    + "<span color=red>Tech:</span> " + currentMeaning.substring(index + 6);
        } else {
            result += "<span color=red>Meaning:</span> " + currentMeaning;
        }
        return result;
    }
}
