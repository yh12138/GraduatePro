package com.example.utils;

import android.content.Context;
	/**      
 * ��Ŀ���ƣ�translate  
 * ʵ�ֹ��ܣ�  ��ʾ������ع�����
 * �����ƣ�DisplayUtil   
 * ��������(�������Ҫ����)
 * �����ˣ����ΰ 
 * E-mail: xujiwei558@126.com
 * ����ʱ�䣺2014��10��21�� ����9:45:18   
 * �޸��ˣ�   
 * �޸�ʱ�䣺   
 * �޸ı�ע��   
 * @version    
 */
public class DisplayUtil {
	/**
		* @��������: px2dip
		* @����: ��pxֵת��Ϊdip��dpֵ
		* @param   @param context
		* @param   @param pxValue
		* @param   @return 
		* @return int 
		* @throws 
		* @author ���ΰ
		* 2014��10��24�� ����9:59:08
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 
		* @��������: dip2px
		* @����: ��dip��dpֵת��Ϊpxֵ
		* @param   @param context
		* @param   @param dipValue
		* @param   @return 
		* @return int 
		* @throws 
		* @author ���ΰ
		* 2014��10��24�� ����9:59:53
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	
	/**
	 * 
		* @��������: px2sp
		* @����: ��pxֵת��Ϊspֵ
		* @param   @param context
		* @param   @param pxValue
		* @param   @return 
		* @return int 
		* @throws 
		* @author ���ΰ
		* 2014��10��24�� ����10:00:36
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}
	
	/**
	 * 
		* @��������: sp2px
		* @����: ��spֵת��Ϊpxֵ
		* @param   @param context
		* @param   @param spValue
		* @param   @return 
		* @return int 
		* @throws 
		* @author ���ΰ
		* 2014��10��24�� ����10:01:01
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
}
