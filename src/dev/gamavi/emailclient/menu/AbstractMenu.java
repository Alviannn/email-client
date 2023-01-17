package dev.gamavi.emailclient.menu;

public abstract class AbstractMenu {

	/**
	 * Any menus that is needed (to be passed) and to be in the next menu
	 * (as if switching to the next page) is stored here.
	 *
	 * It's optional to fill this
	 */
	private AbstractMenu[] nextMenus;

	public AbstractMenu[] getNextMenus() {
		return nextMenus;
	}

	public void setNextMenus(AbstractMenu... switchMenus) {
		this.nextMenus = switchMenus;
	}

	public abstract void show();

}
