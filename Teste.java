import com.microsoft.azure.cognitiveservices.vision.face.*;
import com.microsoft.azure.cognitiveservices.vision.face.models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Teste {
    // Recognition model 3 was released in May 2020
    private static final String RECOGNITION_MODEL3 = RecognitionModel.RECOGNITION_03;

    public static IFaceClient authenticate(String endpoint, String key) {
        return FaceClient.authenticate(new ApiKeyServiceClientCredentials(key)).withEndpoint(endpoint);
    }

    private static List<DetectedFace> detectFaceRecognize(IFaceClient faceClient, String url, String recognition_model) throws Exception {
        // Detect faces from image URL. Since only recognizing, use recognition model 1.
        // We use detection model 2 because we are not retrieving attributes.
        List<DetectedFace> detectedFaces = faceClient.face().detectWithUrl(url, null, null, recognition_model, DetectionModel.DETECTION_02);
        System.out.println(detectedFaces.size() + " face(s) detectada(s) na imagem `" + new File(url).getName() + "`");
        return detectedFaces;
    }

    public static void findSimilar(IFaceClient client, String recognition_model) throws Exception {
        System.out.println("========Encontrar Semelhantes========");
        System.out.println();
        System.out.println("Insira o link da face base (links muito grandes podem causar erros):\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String sourceImageFileName = reader.readLine();
        System.out.println("Insira o link das possíveis faces semelhantes e digite FIM quando acabar (links muito grandes podem causar erros):\n");
        List<String> targetImageFileNames = new ArrayList<>();
        String aux;
        int i = 1;
        do {
            System.out.println("Imagem " + i + ":\n");
            aux = reader.readLine();
            if (!aux.equals("FIM")) {
                targetImageFileNames.add(aux);
            }
            i++;
        } while (!aux.equals("FIM"));

        List<UUID> targetFaceIds = new ArrayList<>();
        for (String targetImageFileName : targetImageFileNames) {
            // Detect faces from target image URL.
            List<DetectedFace> faces = detectFaceRecognize(client, targetImageFileName, recognition_model);
            // Add detected faceId to list of UUIDs.
            targetFaceIds.add(faces.get(0).faceId());
        }

        // Detect faces from source image URL.
        List<DetectedFace> detectedFaces = detectFaceRecognize(client, sourceImageFileName, recognition_model);
        System.out.println();

        // Find a similar face(s) in the list of IDs. Comparing only the first in list for testing purposes.
        List<SimilarFace> similarResults = client.face().findSimilar(detectedFaces.get(0).faceId(), null, null, targetFaceIds);
        i = 1;
        for (SimilarFace similarResult : similarResults) {
            System.out.println("A imagem " + i + " com o FaceID: " + similarResult.faceId() + " é similar à imagem base com a confiança: " + similarResult.confidence() + ".");
            i++;
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        // From your Face subscription in the Azure portal, get your subscription key and endpoint.
        System.out.println("Insira a URL da sua aplicação no Azure:\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String urlServico = reader.readLine();
        System.out.println("Insira a chave da sua aplicação no Azure:\n");
        String chaveServico = reader.readLine();

        // Authenticate.
        IFaceClient client = authenticate(urlServico, chaveServico);
        findSimilar(client, RECOGNITION_MODEL3);
    }
}