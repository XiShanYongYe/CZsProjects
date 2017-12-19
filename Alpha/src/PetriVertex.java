

import java.util.UUID;

/**
 * @description petri网中的顶点集
 * @author xxx
 *
 */
public abstract class PetriVertex{
 
    /**
     * valueֵ     */

    protected String name;
    /*
     * 父类方法，用以对子类对象进行标记
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
		if (obj instanceof PetriVertex) {// ����������Ķ�����D1��������ô�Ͷ��������Ƚ�
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
