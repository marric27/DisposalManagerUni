package it.unisannio.filters;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unisannio.model.Sample;

public class FilterByDistance extends Filter {
    private final Logger LOG = LoggerFactory.getLogger(FilterByDistance.class);

    final int DISTANCE = 2500;

    public FilterByDistance(String name, Filter next) {
        super(name, next);
    }

    @Override
    public Sample filter(Sample s) {
        try {
            if (coordinateDistanceCompare(s)) {
                System.out.println("filtrato per distanza");
                return s;
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean coordinateDistanceCompare(Sample s) throws JsonMappingException, JsonProcessingException {
		GeodeticCalculator geoCalc = new GeodeticCalculator();
		Ellipsoid reference = Ellipsoid.WGS84;
		GlobalPosition disposalPoint = new GlobalPosition(s.getLatitude(), s.getLongitude(), 0.0); // Point A

		// call service
		double realLat = 0, realLon = 0; // collectionpoint coord
		RestTemplate restTemplate = new RestTemplate();

		String result = null;
		try {
			result = restTemplate.getForObject(
					"http://localhost:8081/" + s.getTagID(),
					String.class);
			LOG.info("Coordinate reali restituite dall'altro servizio : {}", result);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Il servizio per la richiesta delle coordinate non risponde");
		}
		ObjectMapper mapper = new ObjectMapper();
		CollectionPoint cp = mapper.readValue(result, CollectionPoint.class);

		realLat = cp.getLat();
		realLon = cp.getLon();

		GlobalPosition realPosition = new GlobalPosition(realLat, realLon, 0.0); // Point B
		double distance = geoCalc.calculateGeodeticCurve(reference, realPosition, disposalPoint)
				.getEllipsoidalDistance(); // Distance between Point A and Point B

		LOG.info("Distanza in metri tra i due punti : {}", distance);
		return distance < DISTANCE ? true : false;
	}

	public static class CollectionPoint {
		private double lat, lon;

		public CollectionPoint() {
		};

		public CollectionPoint(double lat, double lon) {
			super();
			this.lat = lat;
			this.lon = lon;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLon() {
			return lon;
		}

		public void setLon(double lon) {
			this.lon = lon;
		}
	}
    
}
