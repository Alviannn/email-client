package dev.gamavi.emailclient.menu;

public abstract class AbstractMenu {

	/**
	 * All targeted menus to be switched to are stored here.
	 *
	 * It doesn't have to be filled, use it according to the relations
	 * the current menu has with other menus.
	 */
	private AbstractMenu[] switchMenus;

	public AbstractMenu[] getSwitchMenus() {
		return switchMenus;
	}

	public void setSwitchMenus(AbstractMenu... switchMenus) {
		this.switchMenus = switchMenus;
	}

	public abstract void show();

}
