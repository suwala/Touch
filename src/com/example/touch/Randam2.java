package com.example.touch;

public class Randam2 {
	public Integer[] shuffle(Integer[] box){
		for(int i=0;i<box.length;i++){
			int dst = (int)Math.floor(Math.random()*(i+1));
			swap(box,i,dst);
		}
		return box;		
	}
	
	public void swap(Integer[] _box,int _i,int _dst){
		int j = _box[_i];
		_box[_i] = _box[_dst];
		_box[_dst] = j;
	}

}
