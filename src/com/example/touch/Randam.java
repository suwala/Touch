package com.example.touch;


//b�̐��̗v�f������int�z������A�z�񐔂������g�������_���ɐU�蕪����
public class Randam {
	public Integer[] box;
	public Randam(int b){
		if(b<2)
			b=2;//�z��2�ȏ�K�v�Ȃ̂ŏ�������
		box = new Integer[b];
		box[0]=(int)(Math.random()*b);
		for(int i=1;i<b;i++){
			box[i]=(int)(Math.random()*b)+1;
			for(int j=0;j<i;j++){
				if(box[i] == box[j]){
					box[i]=(int)(Math.random()*b)+1;
					j=-1;//++��+1����邽��-1����			
				}
			}
		}
	}
}

