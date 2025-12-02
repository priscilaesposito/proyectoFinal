package model;

public class Pelicula extends Titulo {
    private Video video;
    private int ID;

    // Constructor que inicializa el objeto Video
    public Pelicula() {
        this.video = new Video();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

}
