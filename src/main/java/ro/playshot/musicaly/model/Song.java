package ro.playshot.musicaly.model;

public class Song {
    private String title;
    private Singer singer;
    private int year;
    private String genre;

    public Song(String title, Singer singer, int year, String genre) {
        this.title = title;
        this.singer = singer;
        this.year = year;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Singer getSinger() {
        return singer;
    }

    public void setSinger(Singer singer) {
        this.singer = singer;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", singer=" + singer +
                ", year=" + year +
                ", genre='" + genre + '\'' +
                '}'+ System.lineSeparator();
    }
}
