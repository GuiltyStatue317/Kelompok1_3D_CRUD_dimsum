package pbo.dimsum.model;

public class Dimsum extends EntitasMaster {

    private int stokMatang;

    public Dimsum() {
        super();
    }

    public Dimsum(int idDimsum, String namaDimsum, int stokMatang) {
        super(idDimsum, namaDimsum);
        setStokMatang(stokMatang);
    }

    public int getIdDimsum() {
        return getId();
    }

    public void setIdDimsum(int idDimsum) {
        setId(idDimsum);
    }

    public String getNamaDimsum() {
        return getNama();
    }

    public void setNamaDimsum(String namaDimsum) {
        setNama(namaDimsum);
    }

    public int getStokMatang() {
        return stokMatang;
    }

    public void setStokMatang(int stokMatang) {
        if (stokMatang >= 0) {
            this.stokMatang = stokMatang;
        }
    }

    @Override
    public String getInfoRingkas() {
        return getNama() + " - Matang: " + stokMatang + " pcs";
    }
}
