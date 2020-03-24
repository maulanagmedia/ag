package gmedia.net.id.absenigmedia.penilaian.temp;

public class TempKaryawanModel {
    String nip, nama;
    public TempKaryawanModel(String nip, String nama){
        this.nama =nama;
        this.nip = nip;
    }

    public TempKaryawanModel(String nama){
        this.nama =nama;
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

}
