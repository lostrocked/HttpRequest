import java.io.File;
import java.net.URL;

public class App {
	
	public static void main(String[] args) throws Exception {
		
		URL rightUrl = new URL("http://lvndev010362.bpc.broadcom.net:1505/api/Dcm/VSEs/VSE/124/actions/getMar");
		URL redirectableUrl = new URL("http://lvndev010362.bpc.broadcom.net:1505/api/Dcm/VSEs/VSE/124/actions/getMar"); // redirected to cursos.triadworks.com.br

		Downloader downloader = new Downloader();
		
		System.out.println("Downloading file through right Url...");
		downloader.download(rightUrl, new File("1.mar"));
		
		System.out.println("Downloading file through a redirectable Url...");
		downloader.download(redirectableUrl, new File("main-redirected.css"));
		
	}
}