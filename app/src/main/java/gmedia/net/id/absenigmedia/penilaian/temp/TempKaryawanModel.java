package gmedia.net.id.absenigmedia.penilaian.temp;

public class TempKaryawanModel {
    String nip, nama, id;
    public TempKaryawanModel(String id, String nip, String nama){
        this.id =id;
        this.nama =nama;
        this.nip = nip;
    }
    public TempKaryawanModel(String id, String nama){
        this.nama =nama;
        this.id = id;
    }

    public TempKaryawanModel(String nama){
        this.nama =nama;
    }
    public TempKaryawanModel(){
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return
                "TempKaryawanModel{" +
                        "nama = '" + nama + '\'' +
                        ",id = '" + id + '\'' +
                        ",nip = '" + nip + '\'' +
                        "}";
    }
}
