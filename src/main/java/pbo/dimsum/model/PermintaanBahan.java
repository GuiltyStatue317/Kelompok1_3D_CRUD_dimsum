package pbo.dimsum.model;

import java.sql.Timestamp;

public class PermintaanBahan {

    private String idPermintaan;
    private int idUser;
    private Timestamp tglPermintaan;
    private String status;

    public PermintaanBahan() {
    }

    public PermintaanBahan(String idPermintaan, int idUser, Timestamp tglPermintaan, String status) {
        this.idPermintaan = idPermintaan;
        this.idUser = idUser;
        this.tglPermintaan = tglPermintaan;
        this.status = status;
    }

    public String getIdPermintaan() {
        return idPermintaan;
    }

    public void setIdPermintaan(String idPermintaan) {
        this.idPermintaan = idPermintaan;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Timestamp getTglPermintaan() {
        return tglPermintaan;
    }

    public void setTglPermintaan(Timestamp tglPermintaan) {
        this.tglPermintaan = tglPermintaan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
