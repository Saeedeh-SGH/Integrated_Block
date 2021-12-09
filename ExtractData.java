import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ExtractData {

	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		WebClient webclient = new WebClient(BrowserVersion.FIREFOX_60);
		webclient.getOptions().setJavaScriptEnabled(true);
		HtmlPage page = webclient.getPage("https://myualberta.ualberta.ca/");
		String pageContent = page.asText();
		System.out.println(pageContent);

	}

}
