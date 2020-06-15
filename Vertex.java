/**
 * @author: JASMINE.LIANG(yxl5521)
 * @create: 2020-06-13
 *
 * The vertex of the graph
 * equal to one line in the city.dat
 */
public class Vertex {

  private String city;

  private String state;

  private float latitude;

  private float longitude;

  public Vertex(String city, String state, float latitude, float longitude) {
    this.city = city;
    this.state = state;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public String getState() {
    return this.state;
  }

  public float getLatitude() {
    return this.latitude;
  }

  public float getLongitude() {
    return this.longitude;
  }

  public String getCity() {
    return this.city;
  }

}
