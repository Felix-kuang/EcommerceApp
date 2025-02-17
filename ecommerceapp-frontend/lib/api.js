import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080/api",
});

export async function getProducts(){
    try{
        const response = await api.get("/products");
        return response.data;
    } catch (error){
        throw new Error("Failed to fetch products");
    }
}

export async function createProduct(product){
    try{
        const response = await api.post("/products", product);
        return response.data;
    } catch (error){
        throw new Error("Failed to create product");
    }
}

export default api;