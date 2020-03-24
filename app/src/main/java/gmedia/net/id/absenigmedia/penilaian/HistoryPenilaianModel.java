package gmedia.net.id.absenigmedia.penilaian;

public class HistoryPenilaianModel {
    String tanggal, total;
    public HistoryPenilaianModel(String tanggal, String total){
        this.tanggal =tanggal;
        this.total = total;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
