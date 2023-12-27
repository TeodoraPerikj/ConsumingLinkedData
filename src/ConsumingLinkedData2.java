import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDFS;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConsumingLinkedData2 {

    static final String inputFileName = "C:\\Users\\IGOR\\Desktop\\WBS\\Laboratoriska3\\hifm-dataset-bio2rdf.ttl";

    public static void main(String[] args) {

        Model model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open(inputFileName);

        if(in == null)
            throw new IllegalArgumentException("File: " + inputFileName + " not found.");

        model.read(in, "", "TURTLE");

        model.write(System.out, "TURTLE");
        System.out.println();

        String lisinoprilURI = "http://purl.org/net/hifm/data#978434";

        Resource drug = model.getResource(lisinoprilURI);

        StmtIterator iterator = model.listStatements(new SimpleSelector(drug, RDFS.seeAlso, (RDFNode) null));

        List<Resource> similarDrugs = new ArrayList<>();

        if(iterator.hasNext()) {
            System.out.println("Printing Triples where Lisinopril has a same function with other drugs:");

            while (iterator.hasNext()) {
                Statement statement = iterator.nextStatement();

                Resource subject = statement.getSubject();
                Property predicate = statement.getPredicate();
                RDFNode object = statement.getObject();

                similarDrugs.add(object.asResource());

                System.out.print(subject.toString());
                System.out.print(" - " + predicate.toString() + " - ");
                System.out.print(object.toString());
                System.out.println(".");
            }
        }

        System.out.println(" FIRST DRUG ");

        String firstDrug = similarDrugs.get(0).toString();

        Model firstModel = ModelFactory.createDefaultModel();

        RDFParser.source(firstDrug)
                .acceptHeader("text/turtle")
                .parse(firstModel.getGraph());

        firstModel.write(System.out, "TURTLE");

        System.out.println(" SECOND DRUG ");

        String secondDrug = similarDrugs.get(1).toString();

        Model secondModel = ModelFactory.createDefaultModel();

        RDFParser.source(secondDrug)
                .acceptHeader("text/turtle")
                .parse(secondModel.getGraph());

        secondModel.write(System.out, "TURTLE");

        System.out.println();

        System.out.println("Printing Title and Description for the first drug:");

        Property firstTitle = firstModel.getProperty("http://purl.org/dc/terms/title");

        System.out.println("First Title: " + firstModel.getResource(firstDrug).getProperty(firstTitle).getString());

        Property firstDescription = firstModel.getProperty("http://purl.org/dc/terms/description");

        System.out.println("First Description: " + firstModel.getResource(firstDrug).getProperty(firstDescription).getString());

        System.out.println();

        System.out.println("Printing Title and Description for the second drug:");

        Property secondTitle = secondModel.getProperty("http://purl.org/dc/terms/title");

        System.out.println("Second Title: " + secondModel.getResource(secondDrug).getProperty(secondTitle).getString());

        Property secondDescription = secondModel.getProperty("http://purl.org/dc/terms/description");

        System.out.println("Second Description: " + secondModel.getResource(secondDrug).getProperty(secondDescription).getString());


//        String SPARQLEndpoint = "https://bio2rdf.org/sparql";
//
//        String queryString = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
//                + "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
//                + "prefix dcterms: <http://purl.org/dc/terms/>"
//                + "SELECT ?title ?description "
//                + "WHERE {"
//                + "?drug rdf:type <http://bio2rdf.org/drugbank_vocabulary:Drug>;"
//                + "      rdfs:seeAlso <http://www.drugs.com/lisinopril.html>;"
//                + "      dcterms:title ?title;"
//                + "      dcterms:description ?description ."
//                + "}";
//
//        System.out.println("Using SPARQL query " + queryString);
//
//        Query query = QueryFactory.create(queryString);
//
//        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService(SPARQLEndpoint, query)){
//            ResultSet results = queryExecution.execSelect();
//
//            while (results.hasNext()){
//
//                QuerySolution solution = results.nextSolution();
//                System.out.println("Title " + solution.get("title").toString());
//                System.out.println("Description " + solution.get("description").toString());
//
//            }
//        }
    }
}
