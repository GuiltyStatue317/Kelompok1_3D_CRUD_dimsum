package pbo.dimsum.model;

public class Supplier extends EntitasMaster {

    private String noTelp;
    private String alamat;

    public Supplier() {
        super();
    }

    public Supplier(int idSupplier, String namaSupplier, String noTelp, String alamat) {
        super(idSupplier, namaSupplier);
        this.noTelp = noTelp;
        this.alamat = alamat;
    }

    public int getIdSupplier() {
        return getId();
    }

    public void setIdSupplier(int idSupplier) {
        setId(idSupplier);
    }

    public String getNamaSupplier() {
        return getNama();
    }

    public void setNamaSupplier(String namaSupplier) {
        setNama(namaSupplier);
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    @Override
    public String getInfoRingkas() {
        return getNama() + " (" + noTelp + ")";
    }
}
