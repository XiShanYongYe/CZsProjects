

import java.util.UUID;

/**
 * @description petri缃戜腑鐨勯《鐐归泦
 * @author xxx
 *
 */
public abstract class PetriVertex{
 
    /**
     * value值     */

    protected String name;
    /*
     * 鐖剁被鏂规硶锛岀敤浠ュ瀛愮被瀵硅薄杩涜鏍囪
     * */
    public String getFlag(){
    	return "";
    }
    public PetriVertex(String name) {
        this.name = name;
    }


    public String getName() {

    	return name;
    }

    public void setName(String name) {
        this.name = name;
    }
/*
    @Override
	public boolean equals(Object obj) {
    	PetriVertex d1 = null;
		if (obj instanceof PetriVertex) {// 如果传进来的对象是D1的子类那么就对属性做比较
			d1 = (PetriVertex) obj;
			if (d1.name.equals(this.name))
				return true;
		}
		return false;
	}

	public int hashCode() {
		return (7 * getName().hashCode());
	}*/
    @Override
    public String toString() {
        return "PetriVertex{" +
                "name='" + name + '\'' +
                '}';
    }
}
