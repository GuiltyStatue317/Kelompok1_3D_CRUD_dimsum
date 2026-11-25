package pbo.dimsum.model;

public class DetailPermintaan {

    private int idDetail;
    private String idPermintaan;
    private int idBahan;
    private int jumlahDiambil;

    public DetailPermintaan() {
    }

    public DetailPermintaan(int idDetail, String idPermintaan, int idBahan, int jumlahDiambil) {
        this.idDetail = idDetail;
        this.idPermintaan = idPermintaan;
        this.idBahan = idBahan;
        this.jumlahDiambil = jumlahDiambil;
    }

    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public String getIdPermintaan() {
        return idPermintaan;
    }

    public void setIdPermintaan(String idPermintaan) {
        this.idPermintaan = idPermintaan;
    }

    public int getIdBahan() {
        return idBahan;
    }

    public void setIdBahan(int idBahan) {
        this.idBahan = idBahan;
    }

    public int getJumlahDiambil() {
        return jumlahDiambil;
    }

    public void setJumlahDiambil(int jumlahDiambil) {
        this.jumlahDiambil = jumlahDiambil;
    }
}
