package com.lista.filmpoisk02.model;

import com.lista.filmpoisk02.model.lib.DownloadImage;

import java.util.Objects;

/**
 * Класc POJO - для хранения получаемой инфрмации
 */
public class Page {
    private String imdbID;
    private String title;
    private int year;
    private String production;
    private String poster;
    private String posterImg;

    private String status;
    private int code;

    public Page() {
    }

    public Page(String status, int code) {
        this.status = status;
        this.code = code;
    }

    public Page(String imdbID, String title, int year, String production, String poster, String status, int code) {
        this.imdbID = imdbID;
        this.title = title;
        this.year = year;
        this.production = production;
        setPoster(poster);
        this.status = status;
        this.code = code;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;

        int nPosPoint = poster.lastIndexOf(".");
        if (nPosPoint >= 0 ) {
            this.posterImg = imdbID + poster.substring(nPosPoint);
            new DownloadImage().eval(poster, posterImg );
        } else {
            posterImg = null;
        }
    }

    public String getPosterImg() {
        return posterImg;
    }

    public void setPosterImg(String posterImg) {
        this.posterImg = posterImg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return new StringBuilder().append("Page{")
                .append("imdbID='").append(imdbID).append('\'')
                .append(", title='").append(title).append('\'')
                .append(", year=").append(year).append(", production='").append(production).append('\'')
                .append(", poster='").append(poster).append('\'')
                .append(", posterImg='").append(posterImg).append('\'')
                .append(", status='").append(status).append('\'')
                .append(", code=").append(code).append('}').toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return Objects.equals(imdbID, page.imdbID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imdbID);
    }
}
    /*
    {
    "Title":"Batman",;
    "Year":"1989",;
    "Rated":"PG-13",;
    "Released":"23 Jun 1989",;
    "Runtime":"126 min",;
    "Genre":"Action, Adventure",;
    "Director":"Tim Burton",;
    "Writer":"Bob Kane (Batman characters), Sam Hamm (story), Sam Hamm (screenplay), Warren Skaaren (screenplay)",;
    "Actors":"Michael Keaton, Jack Nicholson, Kim Basinger, Robert Wuhl",;
    "Plot":"The Dark Knight of Gotham City begins his war on crime with his first major enemy being the clownishly homicidal Joker.",;
    "Language":"English, French, Spanish",;
    "Country":"USA, UK",;
    "Awards":"Won 1 Oscar. Another 8 wins & 26 nominations.",
    "Poster":"https://m.media-amazon.com/images/M/MV5BMTYwNjAyODIyMF5BMl5BanBnXkFtZTYwNDMwMDk2._V1_SX300.jpg",;
    "Ratings":[
    {"Source":"Internet Movie Database","Value":"7.5/10"},
    {"Source":"Rotten Tomatoes","Value":"72%"},
    {"Source":"Metacritic","Value":"69/100"}
    ],
    "Metascore":"69",
    "imdbRating":"7.5",
    "imdbVotes":"322,524",
    "imdbID":"tt0096895",
    "Type":"movie",
    "DVD":"25 Mar 1997",
    "BoxOffice":"N/A",
    "Production":"Warner Bros. Pictures",
    "Website":"N/A",
    "Response":"True"}
     */
