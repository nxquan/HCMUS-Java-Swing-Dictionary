package Model;

import java.io.Serializable;
import java.time.LocalDate;

public class StatisticWord implements Serializable {
    private Word _word;
    private LocalDate _date;
    
    public StatisticWord(Word _word, LocalDate _date) {
        this._word = _word;
        this._date = _date;
    }

    public Word getWord() {
        return _word;
    }

    public void setWord(Word _word) {
        this._word = _word;
    }

    public LocalDate getTime() {
        return _date;
    }

    public void setTime(LocalDate _date) {
        this._date = _date;
    }

}
