package Server.Entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Humanplayer {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;
    @Basic
    @Column(name = "dateBirth")
    private Date dateBirth;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "mode")
    private String mode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Humanplayer that = (Humanplayer) o;
        return id == that.id && Objects.equals(dateBirth, that.dateBirth) && Objects.equals(username, that.username) && Objects.equals(mode, that.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateBirth, username, mode);
    }
}
