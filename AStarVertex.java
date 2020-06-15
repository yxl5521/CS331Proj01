/**
 * @author: JASMINE.LIANG(yxl5521)
 * @create: 2020-06-15
 *
 * the node for A* search
 */
public class AStarVertex {

  private String city;

  private float f;

  public AStarVertex(String city, float f) {
    this.city = city;
    this.f = f;
  }

  public String getCity() {
    return city;
  }

  public float getF() {
    return f;
  }
}
