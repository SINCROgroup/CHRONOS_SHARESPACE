package Server.Entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Signatures {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;
    @Basic
    @Column(name = "dateRecord")
    private Date dateRecord;
    @Basic
    @Column(name = "idHumanPlayer")
    private Integer idHumanPlayer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateRecord() {
        return dateRecord;
    }

    public void setDateRecord(Date dateRecord) {
        this.dateRecord = dateRecord;
    }

    public Integer getIdHumanPlayer() {
        return idHumanPlayer;
    }

    public void setIdHumanPlayer(Integer idHumanPlayer) {
        this.idHumanPlayer = idHumanPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signatures that = (Signatures) o;
        return id == that.id && Objects.equals(dateRecord, that.dateRecord) && Objects.equals(idHumanPlayer, that.idHumanPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateRecord, idHumanPlayer);
    }
}
