package plan.lottery.common;

/**
 * 单双玩法
 * 
 * @author wr
 *
 */
public enum PlayTypeDS {

	D("D", "单"), S("S", "双");

	private final String simpleName;
	private final String typeName;

	private PlayTypeDS(String simpleName, String typeName) {
		this.simpleName = simpleName;
		this.typeName = typeName;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public String getTypeName() {
		return typeName;
	}

	public static PlayTypeDS getPlayType(String simpleName) {
		for (PlayTypeDS l : PlayTypeDS.values()) {
			if (l.simpleName.equals(simpleName))
				return l;
		}
		return null;
	}
}