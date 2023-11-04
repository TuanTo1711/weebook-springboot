-- Tạo trigger trước khi chèn dữ liệu vào bảng OrderItem
CREATE OR REPLACE FUNCTION update_product_quantity()
RETURNS TRIGGER AS $$
BEGIN
    -- Kiểm tra nếu số lượng sách lớn hơn 0
    IF ((SELECT quantity FROM product WHERE id = NEW.product_id) - NEW.quantity)>= 0 THEN
        -- Trừ số lượng sách trong bảng Product dựa trên OrderItem
        UPDATE product
        SET quantity = quantity - NEW.quantity
        WHERE id = NEW.product_id;
        RETURN NEW;
    ELSE
        -- Nếu số lượng sản phảẩm không đủ, không thực hiện trừ và quăng ra lỗi
        RAISE EXCEPTION 'Số sản phẩm không đủ';
    END IF;
END
$$ LANGUAGE plpgsql;


-- Tạo trigger cho bảng OrderItem
CREATE TRIGGER update_product_quantity_trigger
    BEFORE INSERT ON order_item
    FOR EACH ROW
    EXECUTE FUNCTION update_product_quantity();