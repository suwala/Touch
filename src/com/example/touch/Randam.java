package com.example.touch;


//bの数の要素を持つint配列を作り、配列数だけ中身をランダムに振り分ける
public class Randam {
	public Integer[] box;
	public Randam(int b){
		if(b<2)
			b=2;//配列が2個以上必要なので書き換え
		box = new Integer[b];
		box[0]=(int)(Math.random()*b);
		for(int i=1;i<b;i++){
			box[i]=(int)(Math.random()*b)+1;
			for(int j=0;j<i;j++){
				if(box[i] == box[j]){
					box[i]=(int)(Math.random()*b)+1;
					j=-1;//++で+1されるため-1を代入			
				}
			}
		}
	}
}

