package com.myproject.v1.controller;

import com.myproject.v1.imageProc.ImageProcessor;
import com.myproject.v1.model.Client;
import com.myproject.v1.model.enums.DomainType;
import com.myproject.v1.model.enums.ProductType;
import com.myproject.v1.model.enums.ResponseType;
import com.myproject.v1.service.ClientService;
import com.myproject.v1.viewmodel.*;
import org.apache.commons.io.FilenameUtils;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.nd4j.linalg.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(path="/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

        @GetMapping(path="domain/verify")
    public ResponseEntity<String> verifyDomain(@RequestParam("url") String domainUrl)  {

        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con =
                    (HttpURLConnection) new URL(domainUrl+ "/verify.txt").openConnection();
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

    @GetMapping(path="{clientId}/code")
    public ResponseEntity<ResultModel<ClientDetailsVM>> getCode (@PathVariable("clientId") String clientId) {

        ResultModel<ClientDetailsVM> resultModel = new ResultModel<>();
        try{
            Optional<Client> client = clientService.getClientByClientId(clientId);
            if(!client.isPresent())
            {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, ""
                );
            }
            Client clientEntity = client.get();

            Charset charset = StandardCharsets.UTF_8;
            String content = "";
            String scriptLocation = Paths.get(System.getProperty("user.dir")).toString()+ "\\code_script\\";

            if(clientEntity.getResponseType().equals(ResponseType.REDIRECT)){
                content  = new String(Files.readAllBytes(Paths.get(scriptLocation + "code_script_redirect.html")), charset);
            }
            else{
                content  = new String(Files.readAllBytes(Paths.get(scriptLocation + "code_script_callback.html")), charset);
            }

            String url =   "http://localhost:8080";
            content = content.replace("{{ClientId}}", clientEntity.getClientId().toString());
            content = content.replace("{{url}}", url);



            ClientDetailsVM data = new ClientDetailsVM();
            data.setClient(clientEntity);
            data.setCode(content);
            resultModel.setData(data);
            return new ResponseEntity<>(resultModel, HttpStatus.OK);

        }
        catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred when processing your request. Please try again"
            );
        }

    }



    @PostMapping(path="register")
    public ResponseEntity<ResultModel<String>> register (@Valid @RequestBody RegistrationModel model)  {

        ResultModel<String> responseObj = new ResultModel<>();
        // verify shop and email exists
        ResultModel<Client> clientResult = clientService.CreateClient(model);

        if(clientResult.isHasError()){
            responseObj.setHasError(true);
            responseObj.setMessage(clientResult.getMessage());
        }
        else{
            responseObj.setHasError(false);
            responseObj.setMessage("Successfully created");
            responseObj.setData(clientResult.getData().getClientId().toString());
        }
        return new ResponseEntity<>(responseObj, HttpStatus.OK);
    }

    @PostMapping(path="{clientId}/search")
    public ResponseEntity<ResultModel<SearchModel>> search (@PathVariable("clientId") String clientId, @Valid @RequestBody MultipartFile file) throws UnsupportedKerasConfigurationException, IOException, InvalidKerasConfigurationException {

        ResultModel<SearchModel> resultModel = new ResultModel<>();

        try{
            Optional<Client> client = clientService.getClientByClientId(clientId);

            if(!client.isPresent())
            {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Not found"
                );
            }

            Client clientEntity = client.get();

            // upload file
            ResultModel<String> fileUploadResult = tryUploadFile(file);
            if(!fileUploadResult.isHasError()){
                String fileName = fileUploadResult.getData();
                File uploadedFile = new File("src/main/resources/public/images/" +fileName );

               // classify image
                String prediction = new ImageProcessor().classifyImage(uploadedFile.getAbsolutePath());
                String color = new ImageProcessor().getImageColor(uploadedFile.getAbsolutePath());

                String[] predArray = prediction.split("%%");

                ProductType productType = null;
                switch (predArray[0]){
                    case "suit" : productType = productType.SUIT; break;
                    case "jacket" : productType = productType.JACKET; break;
                    case "tshirt" : productType = productType.TSHIRT; break;
                    case "sweatshirt" : productType = productType.SWEATSHIRT; break;
                    case "corporate_shirt" : productType = productType.CORPORATE_SHIRT; break;
                }

                // get text
                String tag = "";

                if(productType == null)
                {
                    resultModel.setMessage("Product not found");
                    resultModel.setHasError(true);
                }
                else{
//                    ProductType finalProductType = productType;
//                    boolean productExists = clientEntity.getProductTypes().stream().map(x-> x.getProductType())
//                            .anyMatch(y-> Objects.equals(y, finalProductType));
//
//                    if(!productExists){
//                        resultModel.setMessage("Product not sold");
//                        resultModel.setHasError(true);
//                    }
                }

                if(!resultModel.isHasError()){
                    SearchModel searchModel = new SearchModel();
                    searchModel.setColor(color);
                    searchModel.setAccuracy(predArray[1]);
                    searchModel.setProductType(productType);
                    searchModel.setTag(tag);

                    if(clientEntity.getResponseType().equals(ResponseType.REDIRECT)){
                        searchModel.setResponseURL(clientEntity.getRedirectUrl(productType,color,tag));
                    }
                    clientService.AddSearch(searchModel,client,fileName);
                    resultModel.setData(searchModel);
                }

            }
        }
        catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred when processing your request. Please try again"
            );
        }

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    private static ResultModel<String> tryUploadFile(MultipartFile file) throws IOException {
        ResultModel<String> resultModel = new ResultModel<>();
        String fileName = "";
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            fileName = FilenameUtils.removeExtension(file.getOriginalFilename());

            String ext = FilenameUtils.getExtension(file.getOriginalFilename());

            if(ext.toLowerCase().equals( "jpg") || ext.toLowerCase().equals("jpeg") || ext.toLowerCase().equals("png")){
                fileName = fileName + "_" + timeStamp + "." + ext;
                Path path = Paths.get("src/main/resources/public/images/" + fileName);
                Files.write(path, bytes);
                resultModel.setData(fileName);
            }
            else{
                resultModel.setHasError(true);
                resultModel.setMessage("Invalid file uploaded");
            }

            }
        else{
            resultModel.setHasError(true);
            resultModel.setMessage("No file uploaded");
        }
        return resultModel;
    }



}
