package pbo.dimsum.model;

public abstract class EntitasMaster {

    protected int id;
    protected String nama;

    public EntitasMaster() {
    }

    public EntitasMaster(int id, String nama) {
        this.id = id;
        setNama(nama);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama != null && !nama.isBlank()) {
            this.nama = nama;
        }
    }

    public abstract String getInfoRingkas();
}
