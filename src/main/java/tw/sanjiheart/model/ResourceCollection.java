package tw.sanjiheart.model;

import java.util.Collection;

public class ResourceCollection<T> {

  private long total;

  private Collection<T> resources;

  public ResourceCollection(long total, Collection<T> resources) {
    this.total = total;
    this.resources = resources;
  }

  public long getTotal() {
    return total;
  }

  public Collection<T> getResources() {
    return resources;
  }

}
