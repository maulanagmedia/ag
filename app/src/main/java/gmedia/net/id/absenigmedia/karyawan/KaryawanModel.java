package gmedia.net.id.absenigmedia.karyawan;

public class KaryawanModel {
    String nip, nama, alamat, telp, email, divisi, posisi, foto_profil;
    public KaryawanModel(String nip, String nama, String alamat, String telp, String email, String divisi, String posisi, String foto_profil){
        this.nama =nama;
        this.nip = nip;
        this.alamat = alamat;
        this.divisi = divisi;
        this.email = email;
        this.posisi = posisi;
        this.telp = telp;
        this.foto_profil = foto_profil;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }

    public String getPosisi() {
        return posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }

    public String getFoto_profil() {
        return foto_profil;
    }

    public void setFoto_profil(String foto_profil) {
        this.foto_profil = foto_profil;
    }
}
