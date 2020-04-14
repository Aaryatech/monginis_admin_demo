package com.ats.adminpanel.model.reportv2;

public class AllReportV2Queries {
	/*
	 * 1] Sales Report CODE NAME SALES G.V.N. NET VAL G.R.N NET_VAL IN LAC RET %
	 * SELECT m_franchisee.fr_id, m_franchisee.fr_code, m_franchisee.fr_name,
	 * m_franchisee.fr_city, COALESCE((SELECT SUM(t_bill_header.grand_total) FROM
	 * t_bill_header WHERE t_bill_header.bill_date BETWEEN '2019-02-01' AND
	 * '2019-02-28' AND t_bill_header.del_status=0 AND
	 * m_franchisee.fr_id=t_bill_header.fr_id ), 0) AS sale_value, COALESCE((SELECT
	 * SUM(t_credit_note_header.crn_grand_total) FROM t_credit_note_header WHERE
	 * t_credit_note_header.crn_date BETWEEN '2019-02-01' AND '2019-02-28' AND
	 * t_credit_note_header.is_grn=1 AND
	 * m_franchisee.fr_id=t_credit_note_header.fr_id), 0) AS grn_value,
	 * COALESCE((SELECT SUM(t_credit_note_header.crn_grand_total) FROM
	 * t_credit_note_header WHERE t_credit_note_header.crn_date BETWEEN '2019-02-01'
	 * AND '2019-02-28' AND t_credit_note_header.is_grn=0 AND
	 * m_franchisee.fr_id=t_credit_note_header.fr_id), 0) AS gvn_value from
	 * m_franchisee order by m_franchisee.fr_id
	 * 
	 * 2]
	 * 
	 * //Item wise SELECT m_item.id,m_item.item_name,m_item.item_id,m_item.item_grp1
	 * as cat_id,m_item.item_grp2 as sub_cat_id, m_franchisee.fr_id,
	 * m_franchisee.fr_code, m_franchisee.fr_name, m_franchisee.fr_city,
	 * 
	 * 
	 * COALESCE((SELECT SUM(t_bill_detail.bill_qty) FROM t_bill_header,t_bill_detail
	 * WHERE t_bill_header.bill_date BETWEEN '2019-02-01' AND '2019-02-28' AND
	 * t_bill_header.del_status=0 AND t_bill_detail.bill_no=t_bill_header.bill_no
	 * AND m_item.id=t_bill_detail.item_id AND
	 * m_franchisee.fr_id=t_bill_header.fr_id ), 0) AS sale_qty,
	 * 
	 * 
	 * COALESCE((SELECT SUM(t_bill_detail.grand_total) FROM
	 * t_bill_header,t_bill_detail WHERE t_bill_header.bill_date BETWEEN
	 * '2019-02-01' AND '2019-02-28' AND t_bill_header.del_status=0 AND
	 * t_bill_detail.bill_no=t_bill_header.bill_no AND
	 * m_item.id=t_bill_detail.item_id AND m_franchisee.fr_id=t_bill_header.fr_id ),
	 * 0) AS sale_value,
	 * 
	 * 
	 * COALESCE((SELECT SUM(t_credit_note_details.grn_gvn_qty) FROM
	 * t_credit_note_header,t_credit_note_details WHERE
	 * t_credit_note_header.crn_date BETWEEN '2019-02-01' AND '2019-02-28' AND
	 * t_credit_note_details.crn_id=t_credit_note_header.crn_id AND
	 * t_credit_note_details.item_id=m_item.id AND t_credit_note_header.is_grn=1 AND
	 * m_franchisee.fr_id=t_credit_note_header.fr_id), 0) AS grn_qty,
	 * 
	 * COALESCE((SELECT SUM(t_credit_note_details.grn_gvn_amt) FROM
	 * t_credit_note_header,t_credit_note_details WHERE
	 * t_credit_note_header.crn_date BETWEEN '2019-02-01' AND '2019-02-28' AND
	 * t_credit_note_details.crn_id=t_credit_note_header.crn_id AND
	 * t_credit_note_details.item_id=m_item.id AND t_credit_note_header.is_grn=1 AND
	 * m_franchisee.fr_id=t_credit_note_header.fr_id), 0) AS grn_value,
	 * 
	 * 
	 * COALESCE((SELECT SUM(t_credit_note_details.grn_gvn_qty) FROM
	 * t_credit_note_header,t_credit_note_details WHERE
	 * t_credit_note_header.crn_date BETWEEN '2019-02-01' AND '2019-02-28' AND
	 * t_credit_note_details.crn_id=t_credit_note_header.crn_id AND
	 * t_credit_note_details.item_id=m_item.id AND t_credit_note_header.is_grn=0 AND
	 * m_franchisee.fr_id=t_credit_note_header.fr_id), 0) AS gvn_qty,
	 * 
	 * COALESCE((SELECT SUM(t_credit_note_details.grn_gvn_amt) FROM
	 * t_credit_note_header,t_credit_note_details WHERE
	 * t_credit_note_header.crn_date BETWEEN '2019-02-01' AND '2019-02-28' AND
	 * t_credit_note_details.crn_id=t_credit_note_header.crn_id AND
	 * t_credit_note_details.item_id=m_item.id AND t_credit_note_header.is_grn=0 AND
	 * m_franchisee.fr_id=t_credit_note_header.fr_id), 0) AS gvn_value
	 * 
	 * 
	 * from m_franchisee ,m_item
	 * 
	 * order by m_franchisee.fr_id
	 * 
	 * 
	 * 3] GST Register
	 * 
	 * select t_bill_detail.bill_detail_no, t_bill_header.invoice_no,
	 * t_bill_header.bill_date, m_franchisee.fr_name, m_franchisee.fr_gst_no,
	 * t_bill_detail.bill_no, t_bill_detail.cgst_per, t_bill_detail.sgst_per,
	 * t_bill_detail.cgst_per+sgst_per as tax_per,
	 * ROUND(SUM(t_bill_detail.taxable_amt), 2) as taxable_amt,
	 * ROUND(SUM(t_bill_detail.cgst_rs), 2) as cgst_amt,
	 * ROUND(SUM(t_bill_detail.sgst_rs), 2) as sgst_amt,
	 * ROUND(SUM(t_bill_detail.total_tax), 2) as total_tax,
	 * ROUND(SUM(t_bill_detail.grand_total), 2) as grand_total,
	 * 
	 * ROUND(SUM(t_bill_detail.bill_qty), 2) as bill_qty, m_item_sup.item_hsncd as
	 * hsn_code from t_bill_detail, t_bill_header, m_franchisee,m_item,m_item_sup
	 * where t_bill_header.bill_no=t_bill_detail.bill_no AND
	 * m_item.id=t_bill_detail.item_id AND m_item.id=m_item_sup.item_id AND
	 * t_bill_header.bill_date BETWEEN '2019-02-01' AND '2019-02-28' AND
	 * m_franchisee.fr_id=t_bill_header.fr_id GROUP BY m_item_sup.item_hsncd,
	 * t_bill_detail.bill_no
	 * 
	 * UNION ALL
	 * 
	 * select t_bill_detail.bill_detail_no, t_bill_header.invoice_no,
	 * t_bill_header.bill_date, m_franchisee.fr_name, m_franchisee.fr_gst_no,
	 * t_bill_detail.bill_no, t_bill_detail.cgst_per, t_bill_detail.sgst_per,
	 * t_bill_detail.cgst_per+sgst_per as tax_per,
	 * ROUND(SUM(t_bill_detail.taxable_amt), 2) as taxable_amt,
	 * ROUND(SUM(t_bill_detail.cgst_rs), 2) as cgst_amt,
	 * ROUND(SUM(t_bill_detail.sgst_rs), 2) as sgst_amt,
	 * ROUND(SUM(t_bill_detail.total_tax), 2) as total_tax,
	 * ROUND(SUM(t_bill_detail.grand_total), 2) as grand_total,
	 * 
	 * ROUND(SUM(t_bill_detail.bill_qty), 2) as bill_qty, m_spcake_sup.sp_hsncd as
	 * hsn_code from t_bill_detail, t_bill_header,
	 * m_franchisee,m_sp_cake,m_spcake_sup where
	 * t_bill_header.bill_no=t_bill_detail.bill_no AND
	 * m_sp_cake.sp_id=t_bill_detail.item_id AND m_sp_cake.sp_id=m_spcake_sup.sp_id
	 * AND t_bill_header.bill_date BETWEEN '2019-02-01' AND '2019-02-28' AND
	 * m_franchisee.fr_id=t_bill_header.fr_id GROUP BY m_spcake_sup.sp_hsncd,
	 * t_bill_detail.bill_no
	 * 
	 * 
	 * CRedit note Register
	 * 
	 * SELECT t_credit_note_header.crn_id,t_credit_note_header.crn_date,t_bill_header.invoice_no,t_bill_header.bill_date,
m_franchisee.fr_name,m_franchisee.fr_code,m_franchisee.fr_gst_no,m_item_sup.item_hsncd,
SUM(t_credit_note_details.grn_gvn_qty)crn_qty,SUM(t_credit_note_details.taxable_amt)crn_taxable,t_credit_note_details.cgst_per,t_credit_note_details.sgst_per,t_credit_note_details.igst_per,SUM(t_credit_note_details.sgst_rs) AS sgst_rs ,SUM(t_credit_note_details.cgst_rs) as cgst_rs,SUM(t_credit_note_details.igst_rs) as igst_rs,
SUM(t_credit_note_details.grn_gvn_amt) as crn_amt

FROM t_credit_note_header,t_credit_note_details,t_bill_header,m_franchisee,m_item,m_item_sup
WHERE t_credit_note_header.crn_id=t_credit_note_details.crn_id AND t_credit_note_header.crn_date BETWEEN '2019-02-01' AND '2019-02-28'
AND t_credit_note_header.fr_id=m_franchisee.fr_id AND t_credit_note_details.item_id=m_item.id AND m_item.id=m_item_sup.item_id
GROUP by m_item_sup.item_hsncd

int exInt1;//new for pune billno of bill
	
	String exVarchar1;//new for pune  invno of bill
	
	CRE Reg Final
	m_item Sup
SELECT t_credit_note_header.crn_id,t_credit_note_header.crn_date,t_bill_header.invoice_no,t_bill_header.bill_date,
m_franchisee.fr_name,m_franchisee.fr_code,m_franchisee.fr_gst_no,m_item_sup.item_hsncd,
SUM(t_credit_note_details.grn_gvn_qty)crn_qty,SUM(t_credit_note_details.taxable_amt)crn_taxable,t_credit_note_details.cgst_per,t_credit_note_details.sgst_per,t_credit_note_details.igst_per,SUM(t_credit_note_details.sgst_rs) AS sgst_rs ,SUM(t_credit_note_details.cgst_rs) as cgst_rs,SUM(t_credit_note_details.igst_rs) as igst_rs,
SUM(t_credit_note_details.grn_gvn_amt) as crn_amt

FROM t_credit_note_header,t_credit_note_details,t_bill_header,m_franchisee,m_item,m_item_sup
WHERE t_credit_note_header.crn_id=t_credit_note_details.crn_id AND t_credit_note_header.crn_date BETWEEN '2019-02-01' AND '2019-02-28'
AND t_credit_note_header.fr_id=m_franchisee.fr_id AND t_credit_note_details.item_id=m_item.id AND m_item.id=m_item_sup.item_id
AND t_bill_header.bill_no=t_credit_note_header.ex_int1
GROUP by m_item_sup.item_hsncd,t_credit_note_details.crn_id

m_spcakeSup

SELECT t_credit_note_header.crn_id,t_credit_note_header.crn_date,t_bill_header.invoice_no,t_bill_header.bill_date,
m_franchisee.fr_name,m_franchisee.fr_code,m_franchisee.fr_gst_no,m_spcake_sup.sp_hsncd as hsn_code,
SUM(t_credit_note_details.grn_gvn_qty)crn_qty,SUM(t_credit_note_details.taxable_amt)crn_taxable,t_credit_note_details.cgst_per,t_credit_note_details.sgst_per,t_credit_note_details.igst_per,SUM(t_credit_note_details.sgst_rs) AS sgst_rs ,SUM(t_credit_note_details.cgst_rs) as cgst_rs,SUM(t_credit_note_details.igst_rs) as igst_rs,
SUM(t_credit_note_details.grn_gvn_amt) as crn_amt

FROM t_credit_note_header,t_credit_note_details,t_bill_header,m_franchisee,m_sp_cake,m_spcake_sup
WHERE t_credit_note_header.crn_id=t_credit_note_details.crn_id AND t_credit_note_header.crn_date BETWEEN '2019-02-01' AND '2019-02-28'
AND t_credit_note_header.fr_id=m_franchisee.fr_id AND t_credit_note_details.item_id=m_sp_cake.sp_id AND m_sp_cake.sp_id=m_spcake_sup.sp_id
AND t_bill_header.bill_no=t_credit_note_header.ex_int1
GROUP by m_spcake_sup.sp_hsncd,t_credit_note_details.crn_id
	 */
}
