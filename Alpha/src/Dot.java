

public class Dot {
		public static final int Dot_Place = 1;
		public static final int Dot_Transition = 2;
		public static final int Dot_Arc = 3;
		
		private int type;
		private String id;
		private String name;
		private String markingValue = null;
		private String x;
		private String y;
		private String source;
		private String target;
		
		public Dot(int type) {
			setType(type);
		}
		
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public String getX() {
			return x;
		}
		public String getY() {
			return y;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setX(String x) {
			this.x = x;
		}
		public void setY(String y) {
			this.y = y;
		}

		public String getSource() {
			return source;
		}

		public String getTarget() {
			return target;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public void setTarget(String target) {
			this.target = target;
		}
		
		public String getMarkingValue() {
			return markingValue;
		}
		
		public void setMarkingValue(String markingValue) {
			this.markingValue = markingValue;
		}

}
