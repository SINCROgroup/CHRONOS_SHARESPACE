package Server.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Signatureelement {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;
    @Basic
    @Column(name = "elementPos")
    private Double elementPos;
    @Basic
    @Column(name = "elementVel")
    private Double elementVel;
    @Basic
    @Column(name = "indexSamples")
    private Integer indexSamples;
    @Basic
    @Column(name = "idSignature")
    private Integer idSignature;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getElementPos() {
        return elementPos;
    }

    public void setElementPos(Double elementPos) {
        this.elementPos = elementPos;
    }

    public Double getElementVel() {
        return elementVel;
    }

    public void setElementVel(Double elementVel) {
        this.elementVel = elementVel;
    }

    public Integer getIndexSamples() {
        return indexSamples;
    }

    public void setIndexSamples(Integer indexSamples) {
        this.indexSamples = indexSamples;
    }

    public Integer getIdSignature() {
        return idSignature;
    }

    public void setIdSignature(Integer idSignature) {
        this.idSignature = idSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signatureelement that = (Signatureelement) o;
        return id == that.id && Objects.equals(elementPos, that.elementPos) && Objects.equals(elementVel, that.elementVel) && Objects.equals(indexSamples, that.indexSamples) && Objects.equals(idSignature, that.idSignature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, elementPos, elementVel, indexSamples, idSignature);
    }
}
