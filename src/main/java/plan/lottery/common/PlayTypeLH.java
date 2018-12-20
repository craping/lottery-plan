package plan.lottery.common;

/**
 * 龙虎玩法
 * 
 * @author wr
 *
 */
public enum PlayTypeLH {

	L("L", "龙"), H("H", "虎");

	private final String simpleName;
	private final String typeName;

	private PlayTypeLH(String simpleName, String typeName) {
		this.simpleName = simpleName;
		this.typeName = typeName;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public String getTypeName() {
		return typeName;
	}

	public static PlayTypeLH getPlayType(String simpleName) {
		for (PlayTypeLH l : PlayTypeLH.values()) {
			if (l.simpleName.equals(simpleName))
				return l;
		}
		return null;
	}
}