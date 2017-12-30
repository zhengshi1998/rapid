package util.vectorclock;

import java.util.Vector;

public class VectorClock {

	private int dim;
	private Vector<Integer> clock;

	public VectorClock(int d) {
		this.dim = d;
		this.clock = new Vector<Integer>(dim);
		for (int ind = 0; ind < this.dim; ind++) {
			this.clock.addElement(0);
		}
	}

	public VectorClock(VectorClock fromVectorClock) {
		this.dim = fromVectorClock.getDim();
		this.clock = new Vector<Integer>(dim);
		Vector<Integer> fromClock = fromVectorClock.getClock();
		for (int ind = 0; ind < fromVectorClock.getDim(); ind++) {
			this.clock.addElement((Integer) fromClock.get(ind));
		}
	}

	public int getDim() {
		if(!(this.dim == this.clock.size())){
			throw new IllegalArgumentException("Mismatch in dim and clock size"); 
		}
		return this.dim;
	}

	public Vector<Integer> getClock() {
		return this.clock;
	}

	public String toString() {
		return this.clock.toString();
	}

	/* public void inc(int ind) {
		if(! ((ind < this.dim) && (ind >= 0)) ){
			throw new IllegalArgumentException("You are attempting to access the vector clock with an illegal index");
		}
		int new_clock_val = this.clock.get(ind) + 1;
		this.clock.set(ind, (Integer) new_clock_val);
	} */

	public boolean isZero() {
		boolean itIsZero = true;
		for (int ind = 0; ind < this.dim; ind++) {
			int thisVal = this.clock.get(ind).intValue();
			if (thisVal != 0) {
				itIsZero = false;
				break;
			}
		}
		return itIsZero;
	}

	public boolean isEqual(VectorClock vc) {
		if(! (this.dim == vc.getDim()) ){
			throw new IllegalArgumentException("Mismatch in this.dim and argument.dim"); 
		}
		boolean itIsEqual = true;
		Vector<Integer> vcClock = vc.getClock();
		for (int ind = 0; ind < this.dim; ind++) {
			int thisVal = this.clock.get(ind).intValue();
			int vcVal = vcClock.get(ind).intValue();
			// System.out.println("Comparing: " + thisVal + " | " + vcVal);
			if (thisVal != vcVal) {
				itIsEqual = false;
				break;
			}
		}
		return itIsEqual;
	}

	public boolean isLessThanOrEqual(VectorClock vc) {
		if(! (this.dim == vc.getDim()) ){
			throw new IllegalArgumentException("Mismatch in this.dim and argument.dim"); 
		}
		boolean itIsLessThanOrEqual = true;
		Vector<Integer> vcClock = vc.getClock();
		for (int ind = 0; ind < this.dim; ind++) {
			int thisVal = this.clock.get(ind).intValue();
			int vcVal = vcClock.get(ind).intValue();
			if (!(thisVal <= vcVal)) {
				itIsLessThanOrEqual = false;
				break;
			}
		}
		return itIsLessThanOrEqual;
	}

	public void setToZero() {
		for (int ind = 0; ind < this.dim; ind++) {
			this.clock.set(ind, (Integer) 0 );
		}
	}
	
	public void copyFrom(VectorClock vc) {
		if(! (this.dim == vc.getDim()) ){
			throw new IllegalArgumentException("Mismatch in this.dim and argument.dim"); 
		}
		for (int ind = 0; ind < this.dim; ind++) {
			this.clock.set(ind, (Integer) vc.clock.get(ind));
		}
	}	
	
	private void updateMax2(VectorClock vc) {
		if(! (this.dim == vc.getDim()) ){
			throw new IllegalArgumentException("Mismatch in this.dim and argument.dim"); 
		}
		for (int ind = 0; ind < this.dim; ind++) {
			int this_c = this.clock.get(ind);
			int vc_c = vc.clock.get(ind);
			int max_c = this_c > vc_c ? this_c : vc_c;
			this.clock.set(ind, (Integer) max_c);
		}
	}
	
	public void updateWithMax(VectorClock... vcList) {
		if(! (vcList.length >= 1) ){
			throw new IllegalArgumentException("Insuffiecient number of arguments provided"); 
		}
		for (int i = 1; i < vcList.length; i++) {
			if (vcList[i].equals(this)) throw new IllegalArgumentException("If \'this\' is one of the arguments, then it must be the first");
		}

		//this.setToZero();
		this.copyFrom(vcList[0]);
		for (int i = 1; i < vcList.length; i++) {
			VectorClock vc = vcList[i];
			if (! (this.dim == vc.getDim()) ) {
				throw new IllegalArgumentException("Mismatch in maxVC.dim and vc.dim");
			}
			this.updateMax2(vc);
		}
	}
	
	private void updateMin2(VectorClock vc) {
		if(! (this.dim == vc.getDim()) ){
			throw new IllegalArgumentException("Mismatch in this.dim and argument.dim"); 
		}
		for (int ind = 0; ind < this.dim; ind++) {
			int this_c = this.clock.get(ind);
			int vc_c = vc.clock.get(ind);
			int max_c = this_c < vc_c ? this_c : vc_c;
			this.clock.set(ind, (Integer) max_c);
		}
	}
	
	public void updateWithMin(VectorClock... vcList) {
		if(! (vcList.length >= 1) ){
			throw new IllegalArgumentException("Insuffiecient number of arguments provided"); 
		}
		for (int i = 1; i < vcList.length; i++) {
			if (vcList[i].equals(this)) throw new IllegalArgumentException("If \'this\' is one of the arguments, then it must be the first");
		}

		//this.setToZero();
		this.copyFrom(vcList[0]);
		for (int i = 1; i < vcList.length; i++) {
			VectorClock vc = vcList[i];
			if (! (this.dim == vc.getDim()) ) {
				throw new IllegalArgumentException("Mismatch in maxVC.dim and vc.dim");
			}
			this.updateMin2(vc);
		}
	}
	
	public void setClockIndex(int tIndex, int tValue){
		this.clock.set(tIndex, (Integer) tValue);
	}
	
	/*
	public int increaseDimension(){
		this.dim = this.dim + 1 ;
		this.clock.add(0);
		assert(this.dim == this.clock.size());
		return this.dim;
	}
	
	public int increaseDimension(int val){
		this.dim = this.dim + 1 ;
		this.clock.add(val);
		assert(this.dim == this.clock.size());
		return this.dim;
	}
	*/
	
}