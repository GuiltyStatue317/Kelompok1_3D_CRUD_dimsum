package pbo.dimsum.model;

public class BahanBaku extends EntitasMaster {
    private int stokGudang;
    private String satuan;

    public BahanBaku() {
        super();
    }

    public BahanBaku(int idBahan, String namaBahan, int stokGudang, String satuan) {
        super(idBahan, namaBahan);
        setStokGudang(stokGudang);
        this.satuan = satuan;
    }

    public int getIdBahan() { return getId(); }
    public void setIdBahan(int idBahan) { setId(idBahan); }

    public String getNamaBahan() { return getNama(); }
    public void setNamaBahan(String namaBahan) { setNama(namaBahan); }

    public int getStokGudang() { return stokGudang; }
    public void setStokGudang(int stokGudang) {
        if (stokGudang >= 0) {
            this.stokGudang = stokGudang;
        }
    }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    @Override
    public String getInfoRingkas() {
        return getNama() + " (Stok: " + stokGudang + " " + satuan + ")";
    }

    @Override
    public String toString() {
        return getInfoRingkas();
    }
}
