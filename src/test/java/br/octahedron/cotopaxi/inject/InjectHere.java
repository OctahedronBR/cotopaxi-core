package br.octahedron.cotopaxi.inject;

import br.octahedron.cotopaxi.inject.internal.OnlyPackage;
import br.octahedron.cotopaxi.inject.internal.SuperPackage;

public class InjectHere {

	@Inject
	public OnlyPackage onlyPackage;
	@Inject
	public SuperPackage superPackage;
	
	public void setOnlyPackage(OnlyPackage onlyPackage) {
		this.onlyPackage = onlyPackage;
	}
	
	public void setSuperPackage(SuperPackage superPackage) {
		this.superPackage = superPackage;
	}
	
}

