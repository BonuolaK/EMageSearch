import React, {Component} from "react";

class HomePage extends Component {
    constructor(props) {
        super(props);
        this._apiUrl = 'http://localhost:8080/';
    }


    render() {
        return (
            <div>
                <div className="search_container" >
                    <div className="container">
                        <div className="banner-info">
                            <h3>It is a long established fact that a reader will be distracted by
                                the readable </h3>
                            <div className="search-form">
                                <form action="#" method="post">
                                    <input type="text" placeholder="Search..." name="Search..."/>
                                        <input type="submit" value=" "/>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="content-top ">
                    <div className="container ">
                        <div className=" con-w3l">
                            <div className="col-md-3 m-wthree">
                                <div className="col-m">
                                    <a href="#" data-toggle="modal" data-target="#myModal5" className="offer-img">
                                        <img src="#" className="img-responsive" alt="" />
                                            <div className="offer"><p><span>Offer</span></p></div>
                                    </a>
                                    <div className="mid-1">
                                        <div className="women">
                                            <h6><a href="#">Lays</a>(100 g)</h6>
                                        </div>
                                        <div className="mid-2">
                                            <p><label>$1.00</label><em className="item_price">$0.70</em></p>
                                            <div className="block">
                                                <div className="starbox small ghosting"></div>
                                            </div>
                                            <div className="clearfix"></div>
                                        </div>
                                        <div className="add">
                                            <button className="btn btn-danger my-cart-btn my-cart-b" data-id="5"
                                                    data-name="Lays" data-summary="summary 5" data-price="0.70"
                                                    data-quantity="1" data-image="images/of4.png">Add to Cart
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-md-3 m-wthree">
                                <div className="col-m">
                                    <a href="#" data-toggle="modal" data-target="#myModal6" className="offer-img">
                                        <img src="#" className="img-responsive" alt="" />
                                            <div className="offer"><p><span>Offer</span></p></div>
                                    </a>
                                    <div className="mid-1">
                                        <div className="women">
                                            <h6><a href="#">Kurkure</a>(100 g)</h6>
                                        </div>
                                        <div className="mid-2">
                                            <p><label>$1.00</label><em className="item_price">$0.70</em></p>
                                            <div className="block">
                                                <div className="starbox small ghosting"></div>
                                            </div>
                                            <div className="clearfix"></div>
                                        </div>
                                        <div className="add">
                                            <button className="btn btn-danger my-cart-btn my-cart-b" data-id="6"
                                                    data-name="Kurkure" data-summary="summary 6" data-price="0.70"
                                                    data-quantity="1" data-image="images/of5.png">Add to Cart
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-md-3 m-wthree">
                                <div className="col-m">
                                    <a href="#" data-toggle="modal" data-target="#myModal7" className="offer-img">
                                        <img src="#" className="img-responsive" alt="" />
                                            <div className="offer"><p><span>Offer</span></p></div>
                                    </a>
                                    <div className="mid-1">
                                        <div className="women">
                                            <h6><a href="#">Popcorn</a>(250 g)</h6>
                                        </div>
                                        <div className="mid-2">
                                            <p><label>$2.00</label><em className="item_price">$1.00</em></p>
                                            <div className="block">
                                                <div className="starbox small ghosting"></div>
                                            </div>
                                            <div className="clearfix"></div>
                                        </div>
                                        <div className="add">
                                            <button className="btn btn-danger my-cart-btn my-cart-b" data-id="7"
                                                    data-name="Popcorn" data-summary="summary 7" data-price="1.00"
                                                    data-quantity="1" data-image="images/of6.png">Add to Cart
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-md-3 m-wthree">
                                <div className="col-m">
                                    <a href="#" data-toggle="modal" data-target="#myModal8" className="offer-img">
                                        <img src="images/of7.png" className="img-responsive" alt="" />
                                            <div className="offer"><p><span>Offer</span></p></div>
                                    </a>
                                    <div className="mid-1">
                                        <div className="women">
                                            <h6><a href="single.html">Nuts</a>(250 g)</h6>
                                        </div>
                                        <div className="mid-2">
                                            <p><label>$4.00</label><em className="item_price">$3.50</em></p>
                                            <div className="block">
                                                <div className="starbox small ghosting"></div>
                                            </div>
                                            <div className="clearfix"></div>
                                        </div>
                                        <div className="add">
                                            <button className="btn btn-danger my-cart-btn my-cart-b" data-id="8"
                                                    data-name="Nuts" data-summary="summary 8" data-price="3.50"
                                                    data-quantity="1" data-image="images/of7.png">Add to Cart
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="clearfix"></div>
                        </div>
                    </div>
                </div>
            </div>
        );
    };
}
export default HomePage



