package com.myproject.v1.controller;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.myproject.v1.viewmodel.DataSetImages;
import nu.pattern.OpenCV;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/svc")
@CrossOrigin
public class SvcController {

    @GetMapping(path="images")
    public void getAll() throws JSONException {
     //   OpenCV.loadLocally();
        String API_KEY = "YOUR_API_KEY_GOES_HERE";
        int MAX_RESULTS = 250;
        int GROUP_SIZE = 50;

        List<String> searchParams = new ArrayList<>();
        //searchParams.add("sweatshirt");
       // searchParams.add("business shirt");
        searchParams.add("polo shirt");
       // searchParams.add("jacket");
        //searchParams.add("suit");

        final String uri = "https://api.cognitive.microsoft.com/bing/v7.0/images/search?q=%s&offset=%d&count=50";
        RestTemplate restTemplate = new RestTemplate();

        for (String term:searchParams) {
            List<String> content = new ArrayList<>();

            int inserted = 0;
            int i = 0;
            while (inserted < 250){
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("Ocp-Apim-Subscription-Key", "fc8e79098b174a2899deddd5d717a0ff");
                    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
                    ResponseEntity<String> respEntity = restTemplate.exchange(String.format(uri,term,i), HttpMethod.GET, entity, String.class);

                    String responseString = respEntity.getBody();

                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray array = jsonObject.getJSONArray("value");

                    for(int n = 0; n < array.length(); n++)
                    {
                        if(inserted == 250)
                            break;

                        JSONObject object = array.getJSONObject(n);
                        String url = object.getString("contentUrl");

                        // try download file if works
                        try{
                            URLConnection urlConnection = new URL(url).openConnection();
                            urlConnection.addRequestProperty("User-Agent", "Mozilla");
                            urlConnection.setReadTimeout(5000);
                            urlConnection.setConnectTimeout(5000);

                            try (InputStream inputStream = new URL(url).openStream()){
                                ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
                                String ext = FilenameUtils.getExtension(url);
                                String fileName = "image_" + (inserted + 1)+"."+ (ext.isEmpty() ? "jpg": ext);
                                File targetFile=new File(term.replace(" ","_"), fileName);
                                FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
                                fileOutputStream.getChannel()
                                        .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                                if(targetFile.length() == 0 || (targetFile.length()/1024) <= 20){
                                    targetFile.delete();
                                    continue;
                                }
//                                Mat m = Imgcodecs.imread(fileName);
//                                if(m == null){
//                                    targetFile.delete();
//                                    continue;
//                                }
                                content.add(url);
                                inserted = inserted + 1;
                            }
                            catch (Exception ex){
                                ex.printStackTrace();
                                continue;
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                    i = i + 50;
            }

            writeToFile(term,content);
        }

    }

    @GetMapping(path="download")
    public void downLoad() throws JSONException, IOException {

        String API_KEY = "YOUR_API_KEY_GOES_HERE";
        int MAX_RESULTS = 250;
        int GROUP_SIZE = 50;

        List<String> searchParams = new ArrayList<>();
       // searchParams.add("sweatshirt");
        searchParams.add("dress_shirt");
        searchParams.add("tshirt");
        searchParams.add("jacket");
        searchParams.add("suit");

        for (String term:searchParams) {
          List<String> fileUrls = readImageFiles(term);

//            for (int i = 0; i< fileUrls.size();i++) {
//
//                String url = fileUrls.get(i);
//                URLConnection urlConnection = new URL(url).openConnection();
//                urlConnection.addRequestProperty("User-Agent", "Mozilla");
//                urlConnection.setReadTimeout(5000);
//                urlConnection.setConnectTimeout(5000);
//
//                try (InputStream inputStream = new URL(url).openStream()){
//                    ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
//                    String ext = FilenameUtils.getExtension(fileUrls.get(i));
//                    File targetFile=new File(term, "image_" + (i + 1)+"."+ ext);
//                    FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
//                    fileOutputStream.getChannel()
//                            .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
//                }
//
//            }
        }


    }

    @GetMapping(path="verifyDomain")
    public ResponseEntity<String> verifyDomain(@RequestParam("url") String domainUrl)  {

        try {
          HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con =
                    (HttpURLConnection) new URL(domainUrl+ "/code_script_redirect.html").openConnection();
            con.setRequestMethod("HEAD");

            return new ResponseEntity(con.getResponseCode() == HttpURLConnection.HTTP_OK ? "Found" : "Not-Found", HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error in domain url"
            );
        }
    }


    private void writeToFile(String filename, List<String> stringArray) throws JSONException {
        File file = new File(filename + ".txt");

        JSONArray jsonArray = new JSONArray(stringArray);

        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            String contents = jsonArray.toString();

            writer.write(contents);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readImageFiles(String filename){
        List<String> arrayItems = new ArrayList<>();

        File file = new File(filename + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String st;
            JSONArray jsonArray = null;
            while ((st = reader.readLine()) != null){
                 jsonArray = new JSONArray(st);
            }

            if (jsonArray != null) {
                for (int i=0;i<jsonArray.length();i++){
                    arrayItems.add(jsonArray.getString(i));
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return arrayItems;
    }

    }
