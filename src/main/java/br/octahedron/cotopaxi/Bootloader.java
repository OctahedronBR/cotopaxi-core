/*
 *  This file is part of Cotopaxi.
 *
 *  Cotopaxi is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  Cotopaxi is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the Lesser GNU General Public License
 *  along with Cotopaxi. If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.cotopaxi;

import java.util.Collection;
import java.util.LinkedList;

import br.octahedron.util.Log;
import br.octahedron.util.ReflectionUtil;

/**
 * A simple interface to be executed when application boots.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface Bootloader {

	/**
	 * Informs that Cotopaxi is ready to server and asks Bootloader to boot the application.
	 */
	public abstract void boot();

	/**
	 * This entity is responsible by boot a bunch of {@link Bootloader}
	 */
	static class Booter implements Bootloader {
		private static final Log log = new Log(Booter.class);
		private Collection<Bootloader> bootloaders = new LinkedList<Bootloader>();

		public void addBootloader(String bootloaderClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
			Bootloader bootloader = (Bootloader) ReflectionUtil.getClass(bootloaderClass).newInstance();
			log.debug("Bootloader %s loaded - but no executed yet!", bootloaderClass);
			this.bootloaders.add(bootloader);
		}
		@Override
		public void boot() {
			for (Bootloader bootloader : this.bootloaders) {
				log.info("Executing bootloader %s", bootloader.getClass());
				bootloader.boot();
			}
		}
	}
}
