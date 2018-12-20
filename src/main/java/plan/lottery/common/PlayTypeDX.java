package plan.lottery.common;

/**
 * 大小玩法
 * 
 * @author wr
 *
 */
public enum PlayTypeDX {

	D("D", "大"), X("X", "小");

	private final String simpleName;
	private final String typeName;

	private PlayTypeDX(String simpleName, String typeName) {
		this.simpleName = simpleName;
		this.typeName = typeName;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public String getTypeName() {
		return typeName;
	}

	public static PlayTypeDX getPlayType(String simpleName) {
		for (PlayTypeDX l : PlayTypeDX.values()) {
			if (l.simpleName.equals(simpleName))
				return l;
		}
		return null;
	}
}