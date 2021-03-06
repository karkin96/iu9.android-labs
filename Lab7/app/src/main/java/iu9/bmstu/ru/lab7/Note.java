package iu9.bmstu.ru.lab7;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public final class Note {
    private long id;

    private Date createdAt;
    private Date lastModified;
    private String content;

    public Note() { }

    public Note(long id, Date createdAt, Date lastModified, String content) {
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.content = content;
        this.id = id;
    }

    public Note(String content) {
        this.content = content;
        this.createdAt = Calendar.getInstance().getTime();
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCreatedAtAsString() {
        return dateAsString(createdAt);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public String getLastModifiedAsString() {
        return dateAsString(lastModified);
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(createdAt, note.createdAt) &&
                Objects.equals(lastModified, note.lastModified) &&
                Objects.equals(content, note.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, lastModified, content);
    }

    private static String dateAsString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return sdf.format(date);
    }

}
