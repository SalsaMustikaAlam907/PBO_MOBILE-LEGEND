package heroapp;

public class Hero {
    private int id;
    private String nama;
    private String kategori;
    private String gender;

    // Konstruktor kosong (penting untuk setter/getter & table)
    public Hero() {
    }

    // Konstruktor lengkap (opsional untuk insert cepat)
    public Hero(int id, String nama, String kategori, String gender) {
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        this.gender = gender;
    }

    // Getter dan Setter
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
        this.nama = nama;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Tambahan: Untuk debug atau cetak objek
    @Override
    public String toString() {
        return id + " - " + nama + " (" + kategori + ", " + gender + ")";
    }
}
