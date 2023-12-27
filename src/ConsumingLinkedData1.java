import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDFS;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConsumingLinkedData1 {

    static final String personInput = "https://dbpedia.org/data/Karsten_Warholm.ttl";

    public static void main(String[] args) {

        Model personModel = ModelFactory.createDefaultModel();

        InputStream personIn = FileManager.get().open(personInput);

        if(personIn == null)
            throw new IllegalArgumentException("File: " + personInput + " not found.");

        personModel.read(personIn, "", "TURTLE");

        personModel.write(System.out, "TURTLE");

        Property goldProperty = personModel.getProperty("http://dbpedia.org/property/gold");
        StmtIterator iteratorPerson = personModel.listStatements(new SimpleSelector(null, goldProperty, (RDFNode) null));

        System.out.println();
        List<Resource> events = new ArrayList<>();

        if(iteratorPerson.hasNext()) {
            System.out.println("Printing Triples where Karsten Warholm won gold medal.");

            while (iteratorPerson.hasNext()) {
                Statement statement = iteratorPerson.nextStatement();

                Resource subject = statement.getSubject();
                Property predicate = statement.getPredicate();
                RDFNode object = statement.getObject();

                events.add(subject);

                System.out.print(subject.toString());
                System.out.print(" - " + predicate.toString() + " - ");

                if (object instanceof Resource)
                    System.out.print(object.toString());
                else
                    System.out.print("\"" + object.toString() + "\"");
                System.out.println(".");
            }
        }

        System.out.println();

        String athleticsInput = "https://dbpedia.org/data/Athletics_at_the_2020_Summer_Olympics_%E2%80%93_Men's_400_metres_hurdles.ttl";

        Model athleticsModel = ModelFactory.createDefaultModel();

        InputStream athleticsIn = FileManager.get().open(athleticsInput);

        if(athleticsIn == null)
            throw new IllegalArgumentException("File: " + athleticsInput + " not found.");

        athleticsModel.read(athleticsIn, "", "TURTLE");

        athleticsModel.write(System.out, "TURTLE");
        System.out.println();

        Property venueProperty = athleticsModel.getProperty("http://dbpedia.org/property/venue");

        StmtIterator iteratorAthletics = athleticsModel.listStatements(new SimpleSelector(null, venueProperty, (RDFNode) null));

        if(iteratorAthletics.hasNext()) {
            System.out.println("Printing Triple to discover where is the venue of the selected event.");

            while (iteratorAthletics.hasNext()) {
                Statement statement = iteratorAthletics.nextStatement();

                Resource subject = statement.getSubject();
                Property predicate = statement.getPredicate();
                RDFNode object = statement.getObject();

                System.out.print(subject.toString());
                System.out.print(" - " + predicate.toString() + " - ");
                System.out.println(object.toString() + ".");
            }
        }

        System.out.println();

        String stadiumInput = "https://dbpedia.org/data/Japan_National_Stadium.ttl";

        Model stadiumModel = ModelFactory.createDefaultModel();

        InputStream stadiumIn = FileManager.get().open(stadiumInput);

        if(stadiumIn == null)
            throw new IllegalArgumentException("File: " + stadiumInput + " not found.");

        stadiumModel.read(stadiumIn, "", "TURTLE");

        stadiumModel.write(System.out, "TURTLE");

        String stadiumResource = "http://dbpedia.org/resource/Japan_National_Stadium";
        System.out.println();

        System.out.println("Printing certain facts that describe the Japan National Stadium:");

        Property fullName = stadiumModel.getProperty("http://dbpedia.org/property/fullname");
        System.out.println("Full Name: " + stadiumModel.getResource(stadiumResource).getProperty(fullName).getString());

        Property abstractForStadium = stadiumModel.getProperty("http://dbpedia.org/ontology/abstract");

        NodeIterator abstractIterator = stadiumModel.listObjectsOfProperty(abstractForStadium);

        if(abstractIterator.hasNext()){

            while (abstractIterator.hasNext()){

                RDFNode node = abstractIterator.next();

                if(node.toString().endsWith("@en"))
                    System.out.println("Abstract: " + node.toString());
            }
        }

        Property openingDate = stadiumModel.getProperty("http://dbpedia.org/ontology/openingDate");
        System.out.println("Opening Date: " + stadiumModel.getResource(stadiumResource).getProperty(openingDate).getString());

        Property thumbnail = stadiumModel.getProperty("http://dbpedia.org/ontology/thumbnail");
        System.out.println("Thumbnail: " + stadiumModel.getResource(stadiumResource).getProperty(thumbnail).getResource().toString());

        Property architect = stadiumModel.getProperty("http://dbpedia.org/ontology/architect");
        System.out.println("Architect: " + stadiumModel.getResource(stadiumResource).getProperty(architect).getResource().toString());

        Property owner = stadiumModel.getProperty("http://dbpedia.org/property/owner");
        System.out.println("Owner: " + stadiumModel.getResource(stadiumResource).getProperty(owner).getString());

        NodeIterator commentIterator = stadiumModel.listObjectsOfProperty(RDFS.comment);

        if(commentIterator.hasNext()){

            while (commentIterator.hasNext()){

                RDFNode node = commentIterator.next();

                if(node.toString().endsWith("@en"))
                    System.out.println("Comment: " + node.toString());
            }
        }

    }
}
